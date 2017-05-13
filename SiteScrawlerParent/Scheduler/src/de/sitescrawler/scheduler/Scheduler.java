package de.sitescrawler.scheduler;

import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.inject.Inject;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

@Singleton
public class Scheduler
{
    private static final Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    private ICrawlerLaufService crawlerLaufService;

    @Inject
    private IQuellenManager     quellenManager;

    // @Schedule(second = "0", minute = "*/30", hour = "*", persistent = false)
    public void starteCrawl()
    {
        Scheduler.LOGGER.info("Scheduler starte Crawlvorgang...");
        this.quellenManager.ladeQuellenEin();
        this.crawlerLaufService.crawl();
    }
}
