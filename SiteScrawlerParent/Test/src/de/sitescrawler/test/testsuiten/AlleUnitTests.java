package de.sitescrawler.test.testsuiten;

import org.jglue.cdiunit.CdiRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.sitescrawler.test.unittests.BeispielUnitTest;
import de.sitescrawler.test.unittests.CrawlerUnitTest;
import de.sitescrawler.test.unittests.EmailUnitTest;
import de.sitescrawler.test.unittests.FormatierungUnitTest;
import de.sitescrawler.test.unittests.ReporterUnitTest;

@RunWith(CdiRunner.class)
@Suite.SuiteClasses({ BeispielUnitTest.class, CrawlerUnitTest.class, EmailUnitTest.class,
                      // FirmenverwaltungUnitTest.class,
                      FormatierungUnitTest.class,
                      // NutzerverwaltungUnitTest.class, PDFUnitTest.class,
                      ReporterUnitTest.class
                // SolrUnitTest.class
})

public class AlleUnitTests
{
    // Alle Unit-Tests sind in dieser Test-Suite zusammengefasst.
}
