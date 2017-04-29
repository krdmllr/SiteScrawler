package de.sitescrawler.services.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.model.Artikel; 
import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.solr.SolrService;
import de.sitescrawler.solr.interfaces.ISolrService;


/**
 * @author tobias
 * Verarbeitet eine Quelle.
 * Durchsucht den RSS Feed der Quelle, parst die Artikel und gibt die gefundenen Artikel an Solr weiter.
 */
class Verarbeitung implements Runnable
{
    @Inject
    private ISolrService solrService;
    //Quelle die durchsucht wird
    private Quelle       quelle;

    public Verarbeitung(Quelle quelle)
    {
        this.quelle = quelle;

        //Falls das projekt nicht auf der Serverumgebung läuft und inject fehlt, wird der SolrService manuell initialisiert.
        if (this.solrService == null)
        {
            this.solrService = new SolrService();
        } 
    }

    @Override
    public void run()
    {
    	//TODO tausche durch loggger
        System.out.println(this.quelle.getName() + "...");

        ArtikelZurechtschneiden artikelZurechtschneiden = new ArtikelZurechtschneiden();
        
        try
        {
        	//Parse RSS Feed
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(this.quelle.getRsslink()))); 
            
            //Gehe jeden Eintrag des RSS Feeds durch und crawle die hinterlegte Website nach dem Volltext
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries)
            {
        		//TODO Logger
                System.out.println("Parse: " + entry.getUri());
                
                //Speichere wichtige eigenschaften zwischen.
                String autor = entry.getAuthor();
                String beschreibung = entry.getDescription().getValue();
                String regex = "<a.*/a>";
                beschreibung = beschreibung.replaceAll(regex, "");
                String link = entry.getLink();
                Date veroeffentlichungsDatum = entry.getPublishedDate();
                String titel = entry.getTitle();
                 
                //Crawle die website des Artikels nach dem Text ab.
                List<String> absaetze = new ArrayList<>();
                try
                { 
                    absaetze = artikelZurechtschneiden.getAbsaetze(link, this.quelle);
                }
                catch (UnparsbarException e1)
                {
                    //TODO Add log
                	//TODO Exceptions besser aufschlüsseln
                    e1.printStackTrace();
                    System.out.println("Absätze nicht parsbar");
                }
                
                System.out.println(String.format("Added Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung:%s%n Absätze:%s%n", titel, autor, link,
                                veroeffentlichungsDatum, beschreibung, absaetze.toString()));
                
                //Erstelle einen neuen Artikel aus den gefundenen Daten und übergebe ihn an solr.
                Artikel artikel = new Artikel(veroeffentlichungsDatum, autor, titel, beschreibung, link, absaetze);
                this.solrService.addArtikel(artikel);
            }
        }
        catch (IllegalArgumentException | FeedException | IOException e)
        {
            // TODO Add log
            e.printStackTrace();
        }

        //TODO LOG + von welcher Quelle vong multithreading her
        System.out.println("Crawl ended.");
    }

}
