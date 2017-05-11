package de.sitescrawler.crawler.interfaces;

import java.util.List;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Quelle;

/**
 * 	@author robin
 *	Interface des Crawlers
 */
public interface ICrawlerLaufService
{

    /**
     * Startet den Crawler.
     * Der Crawler durchläuft alle in der Datenbank gesetzten Quellen parallel.
     */
    public void crawl();
    
    /**
     * Startet den Crawlvorgang für die angegebene Quelle und gibt alle gefundenen Artikel zurück.
     * @param quelle Quelle die getestet werden soll.
     * @return Gefundene Artikel.
     */
    public List<Artikel> testeQuelle(Quelle quelle);
}
