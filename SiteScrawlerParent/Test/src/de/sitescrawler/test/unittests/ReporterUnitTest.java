package de.sitescrawler.test.unittests;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import de.sitescrawler.reporter.implementations.ReporterService;
import de.sitescrawler.reporter.interfaces.IReporterService;

public class ReporterUnitTest {
	
	@Test
    public void testeReporter()
    {
        LocalDateTime zeitpunkt = LocalDateTime.now();

        IReporterService reportService = new ReporterService();
         
        reportService.generiereReports(zeitpunkt);
    }
}
