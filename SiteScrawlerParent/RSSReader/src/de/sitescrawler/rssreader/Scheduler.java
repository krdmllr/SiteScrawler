package de.sitescrawler.rssreader;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class Scheduler
{
    @Schedule(minute = "35/5", hour = "*", persistent = false)
    public void printSomething()
    {
        System.out.println("print minute = 15/5, hour= *");
    }

    // @Schedule(minute = "0/30", hour = "*", persistent = false)
    // public void crawlRSS()
    // {
    // System.out.println("CrawlRSS...");
    // String url = "http://newsfeed.zeit.de/";
    // SolrServiceImpl solr = new SolrServiceImpl();
    // try
    // {
    // SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
    // List<SyndEntry> entries = feed.getEntries();
    // entries.forEach(entry -> {
    // String author = entry.getAuthor();
    // String description = entry.getDescription().getValue();
    // String link = entry.getLink();
    // Date publishedDate = entry.getPublishedDate();
    // String title = entry.getTitle();
    //
    // Artikel artikel = new Artikel(publishedDate, author, title, description, link);
    // FilterProfil filterProfil = new FilterProfil(artikel.getAutor(), artikel.getTitel());
    // List<Artikel> sucheArtikel = solr.sucheArtikel(filterProfil);
    // if (sucheArtikel.isEmpty())
    // {
    // solr.addArtikel(artikel);
    // System.out.println(String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung: %s%n",
    // title, author, link,
    // publishedDate, description));
    // }
    // });
    // }
    // catch (IllegalArgumentException | FeedException | IOException e)
    // {
    // e.printStackTrace();
    // }
    // }
}
