package de.sitescrawler.services.crawler;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;

public class Main
{

    public static void main(String[] args)
    {
        ICrawlerLaufService crawler = new CrawlerLaufService();
        crawler.crawl();
    }
}
