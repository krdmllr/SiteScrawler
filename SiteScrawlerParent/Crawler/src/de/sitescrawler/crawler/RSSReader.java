package de.sitescrawler.crawler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class RSSReader {

	public RSSReader() {
		System.out.println("RSSReader initialized");
	}

	@Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
	public void test() {
		System.out.println("schedule triggered");
	}
}
