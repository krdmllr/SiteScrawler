package de.sitescrawler.test.testsuiten;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.sitescrawler.test.unittests.BeispielUnitTest;
import de.sitescrawler.test.unittests.CrawlerUnitTest;
import de.sitescrawler.test.unittests.EmailUnitTest;
import de.sitescrawler.test.unittests.FirmenverwaltungUnitTest;
import de.sitescrawler.test.unittests.FormatierungUnitTest;
import de.sitescrawler.test.unittests.NutzerverwaltungUnitTest;
import de.sitescrawler.test.unittests.PDFUnitTest;
import de.sitescrawler.test.unittests.ReporterUnitTest;
import de.sitescrawler.test.unittests.SolrUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BeispielUnitTest.class, CrawlerUnitTest.class, EmailUnitTest.class, FirmenverwaltungUnitTest.class, FormatierungUnitTest.class,
                      NutzerverwaltungUnitTest.class, PDFUnitTest.class, ReporterUnitTest.class, SolrUnitTest.class })

public class AlleUnitTests
{
    // Alle Unit-Tests sind in dieser Test-Suite zusammengefasst.
}
