package de.sitescrawler.services.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.Quelle;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.SolrServiceImpl;
import de.sitescrawler.solr.interfaces.ISolrService;

public class CrawlerLaufService implements ICrawlerLaufService
{
    @Inject
    private ISolrService solrService;

    public CrawlerLaufService()
    {
        if (this.solrService == null)
        {
            this.solrService = new SolrServiceImpl();
        }
    }

    @Override
    public void crawl()
    {
        for (Quelle q : this.getQuellenAusDatenbank())
        {
            this.verarbeitung(q);
        }
    }

    private List<Quelle> getQuellenAusDatenbank()
    {
        List<Quelle> quellen = new ArrayList<>();
        Quelle testQuelle = new Quelle();
        testQuelle.Url = "http://newsfeed.zeit.de/";
        testQuelle.Name = "Zeit";
        quellen.add(testQuelle);

        return quellen;
    }

    private void verarbeitung(Quelle quelle)
    {
        System.out.println("CrawlRSS...");

        try
        {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(quelle.Url)));
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries)
            {

                System.out.println("Parse: " + entry.getUri());
                String author = entry.getAuthor();
                String description = entry.getDescription().getValue();
                String regex = "<a.*/a>";
                description = description.replaceAll(regex, "");
                String link = entry.getLink();
                Date publishedDate = entry.getPublishedDate();
                String title = entry.getTitle();
                ArtikelZurechtschneiden artikelZurechtschneiden = new ArtikelZurechtschneiden();
                List<String> absaetze = new ArrayList<>();

                try
                {
                    absaetze = artikelZurechtschneiden.getAbsaetze(entry.getUri());
                }
                catch (UnparsbarException e1)
                {
                    // TODO Add log
                    e1.printStackTrace();
                    System.out.println("Absätze nicht parsbar");
                }

                System.out.println(String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n", title, author, link, publishedDate,
                                description));
                Artikel artikel = new Artikel(publishedDate, author, title, description, link, absaetze);

                this.solrService.addArtikel(artikel);
            }
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Crawl ended.");

        // List<Artikel> alleArtikel = this.solrService.getAlleArtikel();
        // alleArtikel.forEach(e -> System.out.println(String.format("Titel: %s Autor: %s", e.getTitel(),
        // e.getAutor())));
        // System.out.println("Ende.");
    }

}
