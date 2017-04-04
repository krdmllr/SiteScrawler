package de.sitescrawler.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterProfil;
import de.sitescrawler.solr.SolrServiceImpl;

public class Main
{

    public static void main(String[] args)
    {
        System.out.println("CrawlRSS...");
        String url = "http://newsfeed.zeit.de/";
        SolrServiceImpl solr = new SolrServiceImpl();
        try
        {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries)
            {
                String author = entry.getAuthor();
                String description = entry.getDescription().getValue();
                String regex = "<a.*/a>";
                description = description.replaceAll(regex, "");
                String link = entry.getLink();
                Date publishedDate = entry.getPublishedDate();
                String title = entry.getTitle();

                Artikel artikel = new Artikel(publishedDate, author, title, description, link);
                FilterProfil filterProfil = new FilterProfil(artikel.getAutor(), artikel.getTitel());
                List<Artikel> sucheArtikel;
                try
                {
                    sucheArtikel = solr.sucheArtikel(filterProfil);
                    if (sucheArtikel.contains(artikel))
                    {
                        System.out.println("Artikel vorhanden.");
                        System.out.println(artikel.getAutor() + " " + artikel.getTitel() + "\n");
                    }
                    else
                    {
                        solr.addArtikel(artikel);
                        System.out.println(String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n", title, author, link,
                                        publishedDate, description));
                    }
                }
                catch (SolrServerException e)
                {
                    e.printStackTrace();
                }
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
