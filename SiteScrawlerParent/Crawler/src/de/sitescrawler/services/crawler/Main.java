package de.sitescrawler.services.crawler;

import java.util.List;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.solr.SolrService;
/**
 * 
 * @author robin
 *	Wird zum manuellen Start des Crawlers zu Testzwecken genutzt.
 */

public class Main
{
    public static void main(String[] args)
    {
//        ICrawlerLaufService crawler = new CrawlerLaufService();
//        crawler.crawl();
    	  SolrService solrService = new SolrService();
    	  solrService.clearSolr();
    	  List<Artikel> alleArtikel = solrService.getAlleArtikel();
    	  alleArtikel.forEach(a -> System.out.println(a.getTitel()));
    }
}
