package de.sitescrawler.services.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.QuellenManager;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;
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
    
    @Inject
    private IQuellenManager quellenManager;

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
    
	@Override
	public List<Artikel> testeQuelle(Quelle quelle) { 
		Verarbeitung verarbeitung = new Verarbeitung(quelle); 
		return verarbeitung.durchsucheQuelle(false);
	}

    /**
     * Liest die Quellen aus der Datenbank aus und gibt sie zurück.
     *
     * @return Quellen aus der Datenbank
     */
    private List<Quelle> getQuellenAusDatenbank()
    { 
    	if(quellenManager == null)
    		quellenManager = new QuellenManager();
    	
        List<Quelle> quellen = quellenManager.getQuellen();

        return quellen;
    }  
}
