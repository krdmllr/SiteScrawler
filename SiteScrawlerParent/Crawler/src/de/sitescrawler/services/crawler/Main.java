package de.sitescrawler.services.crawler;

import java.util.List;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;

public class Main
{

	/*
	 * Wird zum manuellen Start des Crawlers zu Testzwecken genutzt.
	 */
    public static void main(String[] args)
    {
        ICrawlerLaufService crawler = new CrawlerLaufService();
        crawler.crawl();
    }
}
