package de.sitescrawler.services.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IArtikelManager;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;
import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.interfaces.ISolrService;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Tobias, Yvette Verarbeitet eine Quelle. Durchsucht den RSS Feed der Quelle, parst die Artikel und gibt die
 *         gefundenen Artikel an Solr weiter. Crawlt außerdem Twitter nach Tweets und gibt diese an Solr weiter.
 *
 */
@RequestScoped
@Named
public class Verarbeitung
{
    // Globalen Logger holen
    private final static Logger           LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    private ISolrService                  solrService; 
    @Inject
    private ProjectConfig                 projectConfig;
    @Inject
    private IArtikelManager				  artikelManager; 

    public Verarbeitung()
    {
    }

    /**
     * Startet und führt das Zuschneiden der Artikel aus den Quellen aus.
     */
    public List<Artikel> durchsucheQuelle(boolean sendeAnSolr, Quelle quelle)
    {
    	//Überprüfe ob Twitter die Quelle ist und führe die dafür erstellte Methode auf.
    	if(quelle.getQid() == 2)
    		return durchsucheTwitter(sendeAnSolr, quelle);

        List<Artikel> gefundeneArtikel = new ArrayList<>();

        Verarbeitung.LOGGER.log(Level.INFO, quelle.toString() + "...");

        ArtikelZurechtschneiden artikelZurechtschneiden = new ArtikelZurechtschneiden();

        try
        {
            // Parse RSS Feed
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(quelle.getRsslink())));

            // Gehe jeden Eintrag des RSS Feeds durch und crawle die hinterlegte
            // Website nach dem Volltext
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries)
            {
                Verarbeitung.LOGGER.log(Level.INFO, "Parse: " + entry.getUri());

                // Speichere wichtige Eigenschaften zwischen
                String autor = entry.getAuthor();
                String beschreibung = entry.getDescription().getValue();
                String regex = "<a.*/a>";
                beschreibung = beschreibung.replaceAll(regex, "");
                String link = entry.getLink();
                Date veroeffentlichungsDatum = entry.getPublishedDate();
                String titel = entry.getTitle();

                // Crawle die Website des Artikels nach dem Text ab
                List<String> absaetze = new ArrayList<>();
                try
                {
                    absaetze = artikelZurechtschneiden.getAbsaetze(link, quelle);
                }
                catch (UnparsbarException e1)
                {
                    Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim Parsen der Absätze: " + entry.getUri());
                    // TODO Exceptions besser aufschlüsseln
                    e1.printStackTrace();
                }

                Verarbeitung.LOGGER.log(Level.INFO, String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n Absätze:%s%n", titel,
                                autor, link, veroeffentlichungsDatum, beschreibung, absaetze.toString()));

                // Erstelle einen neuen Artikel aus den gefundenen Daten und
                // übergebe ihn an Solr
                Artikel artikel = new Artikel(veroeffentlichungsDatum, autor, titel, beschreibung, link, absaetze, quelle);

                gefundeneArtikel.add(artikel);

            }
            persistiereArtikel(sendeAnSolr, gefundeneArtikel);
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim Parsen der Seite!");
            e.printStackTrace();
        }

        Verarbeitung.LOGGER.log(Level.INFO, "Crawl von " + quelle.getName() + " fertig. " + gefundeneArtikel.size() + " Artikel gefunden.");
        return gefundeneArtikel;
    }

    /**
     * Durchsucht die Twitter Trends in Deutschland und gibt für jeden Trend bis zu 100 Tweets zurück.
     *
     * @param sendeAnSolr
     *            gibt an ob die Artikel in Solr geschrieben werden sollen
     * @return gefundeneArtikel Liste der gefundenen Artikel
     */
    private List<Artikel> durchsucheTwitter(boolean sendeAnSolr, Quelle twitterQuelle)
    {
        List<Artikel> gefundeneArtikel = new ArrayList<>();

        String consumerKey = this.projectConfig.getConsumerKey();
        String consumerSecret = this.projectConfig.getConsumerSecret();
        String accessToken = this.projectConfig.getAccessToken();
        String accessTokenSecret = this.projectConfig.getAccessTokenSecret();

        // Authentication
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(accessToken)
                        .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try
        {
            // Trends für Deutschland mittels WOEID holen
            Trends trends = twitter.getPlaceTrends(23424829);

            // Für jeden Trend neue Query erzeugen und die Tweets lesen
            for (int i = 0; i < trends.getTrends().length; i++)
            {
                String suchstring = trends.getTrends()[i].getQuery();
                Query query = new Query(suchstring);
                QueryResult result;
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();

                // Artikel aus den Tweets erzeugen
                for (Status tweet : tweets)
                {

                    // TODO: überprüfen ob Artikel richtig erstellt
                	String url= "https://twitter.com/" + tweet.getUser().getScreenName() 
                		    + "/status/" + tweet.getId();

                    Artikel artikel = new Artikel(tweet.getCreatedAt(), tweet.getUser().getScreenName(), "Tweet" + tweet.getId(), tweet.getText(),
                    		url, twitterQuelle);
                    artikel.setFavoritenzahl(tweet.getFavoriteCount());
                    artikel.setRetweetzahl(tweet.getRetweetCount());
                    gefundeneArtikel.add(artikel);
                    Verarbeitung.LOGGER.log(Level.INFO, "Tweet gefunden: @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                }
            }
            persistiereArtikel(sendeAnSolr, gefundeneArtikel);
        }
        catch (TwitterException e)
        {
            Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim holen der Trends!");
            e.printStackTrace();
        }

        Verarbeitung.LOGGER.log(Level.INFO, "Crawl von Twitter abgeschlossen. Tweets gefunden: " + gefundeneArtikel.size());
        return gefundeneArtikel;
    }
    
    private void persistiereArtikel(boolean sendeAnSolr, List<Artikel> gefundeneArtikel)
    {
        if (sendeAnSolr)
        {
            this.solrService.addArtikel(gefundeneArtikel);
        }
    }
}
