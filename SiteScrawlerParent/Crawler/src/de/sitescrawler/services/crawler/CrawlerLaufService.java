package de.sitescrawler.services.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.solr.SolrService;
import de.sitescrawler.solr.interfaces.ISolrService;

/**
 * @author tobias, Yvette
 * Logik zum Starten des Crawl-Vorgangs.
 */
public class CrawlerLaufService implements ICrawlerLaufService
{
    @Inject
    private ISolrService solrService;

    ExecutorService      threadPool = Executors.newFixedThreadPool(5);

    public CrawlerLaufService()
    {
        // Falls inject nicht funktioniert (Ausführung ohne Serverumgebung),
        // wird der SolrService manuell initialisiert.
        if (this.solrService == null)
        {
            this.solrService = new SolrService();
        }
    }

    /**
     * Crawlt durch die Quellen, die in der Datenbank abgelegt sind.
     */
    @Override
    public void crawl()
    {
        for (Quelle q : this.getQuellenAusDatenbank())
        {
            Runnable run = new Verarbeitung(q);
            this.threadPool.submit(run);
        }
    }

    /**
     * Liest die Quellen aus der Datenbank aus und gibt sie zurück.
     *
     * @return Quellen aus der Datenbank
     */
    private List<Quelle> getQuellenAusDatenbank()
    {
        //TODO Dummy-Daten durch Produktiv-Daten ersetzen.
        List<Quelle> quellen = new ArrayList<>();
        Quelle testQuelle = new Quelle();
        testQuelle.setRsslink("http://newsfeed.zeit.de/");
        testQuelle.setName("Zeit");
        testQuelle.setTagOderId("article-page");
        quellen.add(testQuelle);
        Quelle testQuelle2 = new Quelle();
        testQuelle2.setRsslink("http://www.spiegel.de/schlagzeilen/index.rss");
        testQuelle2.setName("Spiegel");
        testQuelle2.setTagOderId("spArticleContent");
        quellen.add(testQuelle2);

        return quellen;
    }

}
