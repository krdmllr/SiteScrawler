package de.sitescrawler.services.crawler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

// funktioniert nicht warum auch immer
@Singleton
public class RSSReader
{

    @Schedule(second = "*/2", minute = "*", hour = "*", persistent = false)
    public void test()
    {
        System.out.println("schedule triggered");
    }
}
