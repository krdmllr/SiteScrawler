package de.sitescrawler.test.unittests;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.sitescrawler.jpa.management.FiltergruppenZugriffsManager;
import de.sitescrawler.report.ReporterService;

@RunWith(CdiRunner.class)
@AdditionalClasses(FiltergruppenZugriffsManager.class)
public class ReporterUnitTest
{

    @Inject
    private ReporterService reportService;

    @Test
    public void testeReporter()
    {
        LocalDateTime zeitpunkt = LocalDateTime.now();

        reportService.generiereReports(zeitpunkt);
    }
}
