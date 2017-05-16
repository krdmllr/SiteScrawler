package de.sitescrawler.test.unittests;

import java.time.LocalDateTime;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.sitescrawler.report.IReportService;
import de.sitescrawler.report.ReporterService;

@RunWith(CdiRunner.class)
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
