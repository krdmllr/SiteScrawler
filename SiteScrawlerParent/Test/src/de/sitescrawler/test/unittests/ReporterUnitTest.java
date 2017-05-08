package de.sitescrawler.test.unittests;

import java.time.LocalDateTime;

import org.junit.Test;

import de.sitescrawler.report.IReportService;
import de.sitescrawler.report.ReporterService;

public class ReporterUnitTest
{

    @Test
    public void testeReporter()
    {
        LocalDateTime zeitpunkt = LocalDateTime.now();

        IReportService reportService = new ReporterService();

        reportService.generiereReports(zeitpunkt);
    }
}
