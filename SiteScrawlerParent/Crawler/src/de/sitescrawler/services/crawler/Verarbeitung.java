package de.sitescrawler.services.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.SolrService;
import de.sitescrawler.solr.interfaces.ISolrService;

/**
 * @author Tobias, Yvette
 * Verarbeitet eine Quelle. Durchsucht den RSS Feed der Quelle,
 * parst die Artikel und gibt die gefundenen Artikel an Solr weiter.
 *
 */
class Verarbeitung implements Runnable
{
    // Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    private ISolrService solrService;
    // Quelle, die durchsucht wird
    private Quelle       quelle;

    public Verarbeitung(Quelle quelle)
    {
        this.quelle = quelle;

        // Falls das Projekt nicht auf der Serverumgebung läuft und inject fehlt,
        // wird der SolrService manuell initialisiert.
        if (this.solrService == null)
        {
            this.solrService = new SolrService();
        }
    }

    /**
     * Startet und führt das Zuschneiden der Artikel aus den Quellen aus.
     */
    @Override
    public void run()
    {
        Verarbeitung.LOGGER.log(Level.INFO, this.quelle.toString() + "...");

        ArtikelZurechtschneiden artikelZurechtschneiden = new ArtikelZurechtschneiden();

        try
        {
            // Parse RSS Feed
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(this.quelle.getRsslink())));

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
                    absaetze = artikelZurechtschneiden.getAbsaetze(link, this.quelle);
                }
                catch (UnparsbarException e1)
                {
                    Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim Parsen der Absätze: " + entry.getUri());
                    // TODO Exceptions besser aufschlüsseln
                    e1.printStackTrace();
                }

                Verarbeitung.LOGGER.log(Level.INFO, String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n Absätze:%s%n",
                                titel, autor, link, veroeffentlichungsDatum, beschreibung, absaetze.toString()));

                // Erstelle einen neuen Artikel aus den gefundenen Daten und übergebe ihn an Solr
                Artikel artikel = new Artikel(veroeffentlichungsDatum, autor, titel, beschreibung, link, absaetze);
                this.solrService.addArtikel(artikel);
            }
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            Verarbeitung.LOGGER.log(Level.SEVERE, "Fehler beim Parsen der Seite!");
            e.printStackTrace();
        }

        // TODO: Quelle im Log mitaufnehmen?
        Verarbeitung.LOGGER.log(Level.INFO, "Crawl fertig");
    }

}
