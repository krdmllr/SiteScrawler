package de.sitescrawler.crawler.interfaces;

public interface ICrawlerLaufService
{

    /**
     * Startet den Crawler.
     * Der Crawler durchläuft alle in der Datenbank gesetzten Quellen parallel.
     */
    public void crawl();
}
