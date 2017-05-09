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
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.interfaces.ISolrService;

/**
 * @author Tobias, Yvette Verarbeitet eine Quelle. Durchsucht den RSS Feed der Quelle, parst die Artikel und gibt die
 *         gefundenen Artikel an Solr weiter.
 *
 */
@RequestScoped
@Named
class Verarbeitung
{
    // Globalen Logger holen
    private final static Logger           LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    private ISolrService                  solrService;
    @Inject
    private IFiltergruppenZugriffsManager filtergruppenZugriffsManager;

    public Verarbeitung()
    {
    }

    /**
     * Startet und führt das Zuschneiden der Artikel aus den Quellen aus.
     */
    public List<Artikel> durchsucheQuelle(boolean sendeAnSolr, Quelle quelle)
    {

        List<Artikel> gefundeneArtikel = new ArrayList<>();

        Verarbeitung.LOGGER.log(Level.INFO, quelle.toString() + "...");

        ArtikelZurechtschneiden artikelZurechtschneiden = new ArtikelZurechtschneiden();

        try
        {
            // Parse RSS Feed
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(quelle.getRsslink())));

            // Gehe jeden Eintrag des RSS Feeds durch und crawle die hinterlegte Website nach dem Volltext
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
                    Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim Parsen der Absätze: " + entry.getUri(), e1);
                    // TODO Exceptions besser aufschlüsseln
                }

                Verarbeitung.LOGGER.log(Level.INFO, String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n Absätze:%s%n", titel,
                                autor, link, veroeffentlichungsDatum, beschreibung, absaetze.toString()));

                // Erstelle einen neuen Artikel aus den gefundenen Daten und übergebe ihn an Solr
                Artikel artikel = new Artikel(veroeffentlichungsDatum, autor, titel, beschreibung, link, absaetze);

                gefundeneArtikel.add(artikel);

            }
            if (sendeAnSolr)
            {
                this.solrService.addArtikel(gefundeneArtikel);
            }
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim Parsen der Seite!", e);
        }

        Verarbeitung.LOGGER.log(Level.INFO, "Crawl von " + quelle.getName() + " fertig. " + gefundeneArtikel.size() + " Artikel gefunden.");
        return gefundeneArtikel;
    }

    public List<Artikel> durchsucheTwitter()
    {
        List<Artikel> gefundeneArtikel = new ArrayList<>();
        // TODO Robin
        return gefundeneArtikel;
    }

}
