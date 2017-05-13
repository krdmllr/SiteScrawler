package de.sitescrawler.services.crawler;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.QuellenManager;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

/**
 * @author tobias, Yvette 
 * Logik zum Starten des Crawl-Vorgangs.Implementierung des Crawler Interfaces
 */
@ApplicationScoped
public class CrawlerLaufService implements ICrawlerLaufService
{

    @Inject
    private IQuellenManager quellenManager;

    @Inject
    private Verarbeitung    verarbeitung;

    public CrawlerLaufService()
    {
    }

    /**
     * Crawlt durch die Quellen, die in der Datenbank abgelegt sind.
     */
    @Override
    public void crawl()
    {
        for (Quelle q : this.getQuellenAusDatenbank())
        {
            this.verarbeitung.durchsucheQuelle(true, q);
        }
    }
    
    /**
     * Testet eine bestimmte Quelle und durchsucht diese
     */
    @Override
    public List<Artikel> testeQuelle(Quelle quelle)
    {
        return this.verarbeitung.durchsucheQuelle(false, quelle);
    }

    /**
     * Liest die Quellen aus der Datenbank aus und gibt sie zurück.
     *
     * @return Quellen aus der Datenbank
     */
    private List<Quelle> getQuellenAusDatenbank()
    {
        if (this.quellenManager == null)
        {
            this.quellenManager = new QuellenManager();
        }

        List<Quelle> quellen = this.quellenManager.getQuellen();

        return quellen;
    }
}
