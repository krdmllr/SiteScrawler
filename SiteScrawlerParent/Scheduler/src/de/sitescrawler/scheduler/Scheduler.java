package de.sitescrawler.scheduler;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;
import de.sitescrawler.report.IReportService;

@Singleton
public class Scheduler
{
    private static final Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    private ICrawlerLaufService crawlerLaufService;

    @Inject
    private IQuellenManager     quellenManager;

    @Inject
    private IReportService      reportService;

    @Schedule(second = "0", minute = "*/30", hour = "*", persistent = false)
    public void starteCrawl()
    {
        Scheduler.LOGGER.info("Scheduler starte Crawlvorgang...");
        this.quellenManager.ladeQuellenEin();
        this.crawlerLaufService.crawl();
    }

    @Schedule(second = "0", minute = "50", hour = "*", persistent = false)
    public void erstelleReports()
    {
        Scheduler.LOGGER.info("Scheduler starte Reporting...");
        this.reportService.generiereReports(LocalDateTime.now());
    }

}
