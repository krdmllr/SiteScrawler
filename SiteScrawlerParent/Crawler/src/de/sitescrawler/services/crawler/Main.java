package de.sitescrawler.services.crawler;

import java.util.List;

import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;

public class Main
{

    public static void main(String[] args)
    {
        try
        {
            List<String> absätze = new ArtikelZurechtschneiden().getAbsaetze(
                            "http://www.spiegel.de/sport/fussball/dfb-pokal-borussia-moenchengladbach-wirkte-wie-gelaehmt-gegen-frankfurt-a-1144859.html#ref=rss",
                            "spArticleContent");
            System.out.println("bla");
        }
        catch (UnparsbarException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ICrawlerLaufService crawler = new CrawlerLaufService();
        // crawler.crawl();
    }
}
