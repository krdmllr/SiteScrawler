package de.sitescrawler.services.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.model.Artikel;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.SolrServiceImpl;

public class Main
{

    public static void main(String[] args)
    {

        try
        {
            List<String> absätze = new ArtikelZurechtschneiden()
                            .getVolltextartikel("http://www.zeit.de/mobilitaet/2017-04/streetscooter-deutsche-post-elektromobilitaet");
            System.out.println(absätze.toString());
        }
        catch (UnparsbarException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Main.verarbeitung();
    }

    public static void verarbeitung()
    {
        System.out.println("CrawlRSS...");
        String url = "http://newsfeed.zeit.de/";
        SolrServiceImpl solr = new SolrServiceImpl();
        // solr.clearSolr();
        try
        {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
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
                    absaetze = artikelZurechtschneiden.getVolltextartikel(entry.getUri());
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

                // solr.addArtikel(artikel);
            }
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Crawl ended.");

        List<Artikel> alleArtikel = solr.getAlleArtikel();
        alleArtikel.forEach(e -> System.out.println(String.format("Titel: %s Autor: %s", e.getTitel(), e.getAutor())));
        System.out.println("Ende.");
    }
}
