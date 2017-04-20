package de.sitescrawler.crawler.interfaces;

import de.sitescrawler.model.Quellen;

public interface ICrawlerLaufService
{

    /**
     * Startet den Crawler.
     *
     */
    public void crawl(Quellen quellen);
}
