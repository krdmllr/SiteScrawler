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

public class Main {

	public static void main(String[] args) {
		String url = "http://newsfeed.zeit.de/";
		try {
			SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(url)));
			List<SyndEntry> entries = feed.getEntries();
			entries.forEach(entry -> {
				String author = entry.getAuthor();
				String description = entry.getDescription().getValue();
				String link = entry.getLink();
				Date publishedDate = entry.getPublishedDate();
				String title = entry.getTitle();
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

	}

}
