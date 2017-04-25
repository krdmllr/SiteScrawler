package de.sitescrawler.services.crawler;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;

public class Main
{

    public static void main(String[] args)
    {

        // try
        // {
        // List<String> absätze = new ArtikelZurechtschneiden()
        // .getAbsaetze("http://www.zeit.de/mobilitaet/2017-04/streetscooter-deutsche-post-elektromobilitaet");
        // System.out.println(absätze.toString());
        // }
        // catch (UnparsbarException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // Main.verarbeitung();

        ICrawlerLaufService crawler = new CrawlerLaufService();
        crawler.crawl();
    }
}
