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

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.Quelle;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.SolrServiceImpl;
import de.sitescrawler.solr.interfaces.ISolrService;

class Verarbeitung implements Runnable
{
    @Inject
    private ISolrService solrService;
    private Quelle       quelle;

    public Verarbeitung(Quelle quelle)
    {
        this.quelle = quelle;

        if (this.solrService == null)
        {
            this.solrService = new SolrServiceImpl();
        }

    }

    @Override
    public void run()
    {

        System.out.println(this.quelle.Name + "...");

        try
        {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(this.quelle.Url)));
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
                    absaetze = artikelZurechtschneiden.getAbsaetze(link, this.quelle.TagOderId);
                }
                catch (UnparsbarException e1)
                {
                    // TODO Add log
                    e1.printStackTrace();
                    System.out.println("Absätze nicht parsbar");
                }
                System.out.println(String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n Absätze:%s%n", title, author, link,
                                publishedDate, description, absaetze.toString()));
                Artikel artikel = new Artikel(publishedDate, author, title, description, link, absaetze);
                this.solrService.addArtikel(artikel);
            }
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Crawl ended.");

    }

}
