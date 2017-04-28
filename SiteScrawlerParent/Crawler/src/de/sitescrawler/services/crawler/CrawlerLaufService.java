package de.sitescrawler.services.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.model.Quelle;
import de.sitescrawler.solr.SolrService;
import de.sitescrawler.solr.interfaces.ISolrService;

public class CrawlerLaufService implements ICrawlerLaufService
{
    @Inject
    private ISolrService solrService;
    
    ExecutorService      threadPool = Executors.newFixedThreadPool(5);

    public CrawlerLaufService()
    {
    	//Falls inject nicht funktioniert (Ausführung ohne Serverumgebung) wird der SolrService manuell initialisiert.
        if (this.solrService == null)
        {
            this.solrService = new SolrService();
        }
    }

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
     * Ließt die Quellen aus der Datenbank aus und gibt Sie zurück.
     *
     * @return Quellen aus der Datenbank
     */
    private List<Quelle> getQuellenAusDatenbank()
    {
    	//TODO Aktuell Dummy Quellen, wird durch Quellen aus Datenbank ersetzt.
        List<Quelle> quellen = new ArrayList<>();
        Quelle testQuelle = new Quelle();
        testQuelle.Url = "http://newsfeed.zeit.de/";
        testQuelle.Name = "Zeit";
        testQuelle.TagOderId = "article-page";
        quellen.add(testQuelle);
        Quelle testQuelle2 = new Quelle();
        testQuelle2.Url = "http://www.spiegel.de/schlagzeilen/index.rss";
        testQuelle2.Name = "Spiegel";
        testQuelle2.TagOderId = "spArticleContent";
        quellen.add(testQuelle2);

        return quellen;
    }

}
