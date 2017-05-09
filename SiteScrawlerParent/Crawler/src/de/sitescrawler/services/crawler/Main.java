package de.sitescrawler.services.crawler;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
/**
 * 
 * @author robin
 *	Wird zum manuellen Start des Crawlers zu Testzwecken genutzt.
 */

public class Main
{
    public static void main(String[] args)
    {
        ICrawlerLaufService crawler = new CrawlerLaufService();
        crawler.crawl();
    }
}
