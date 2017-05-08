package de.sitescrawler.scheduler;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

import de.sitescrawler.jpa.Nutzer;

@Singleton
public class Scheduler
{
    @Schedule(second = "*", minute = "*", hour = "*", persistent = false)
    public void tuWas()
    {
        Nutzer nutzer = new Nutzer("bla@blub.net", "pwd");
        System.out.println(nutzer.getEmail());
    }
}
