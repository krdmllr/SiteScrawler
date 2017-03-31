package de.sitescrawler.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterProfil;
import de.sitescrawler.solr.SolrServiceImpl;
import de.sitescrawler.utility.DateUtils;

public class Main {

	public static void main(String[] args) {
		String url = "http://newsfeed.zeit.de/";
		SolrServiceImpl solr = new SolrServiceImpl();
		solr.clearSolr();
		try {
			SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
			List<SyndEntry> entries = feed.getEntries();
			entries.forEach(entry -> {
				String author = entry.getAuthor();
				String description = entry.getDescription().getValue();
				String link = entry.getLink();
				Date publishedDate = entry.getPublishedDate();
				String title = entry.getTitle();
				Artikel artikel = new Artikel(publishedDate, author, title, description, link);
				solr.addArtikel(artikel);
				System.out.println(String.format("Titel: %s%n Autor: %s%n Link: %s%n Datum: %s%n Beschreibung: %s%n", title,
						author, link, publishedDate, description));
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FilterProfil filterProfil = new FilterProfil();
		filterProfil.addTag("*:*");
		List<Artikel> result = solr.sucheArtikel(filterProfil);
		result.forEach(e->System.out.println(e.getAutor()+e.getBeschreibung()+e.getLink()+e.getTitel()+e.getErstellungsdatum()));
	}

}
