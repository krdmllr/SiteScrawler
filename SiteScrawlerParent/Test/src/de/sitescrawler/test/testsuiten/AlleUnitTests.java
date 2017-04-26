package de.sitescrawler.test.testsuiten;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.sitescrawler.test.unittests.BeispielUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BeispielUnitTest.class,
    //CrawlerUnitTest.class,
    //FirmenverwaltungUnitTest.class,
    //NutzerverwaltungUnitTest.class
})

public class AlleUnitTests
{
    // Alle Unit-Tests sind in dieser Test-Suite zusammengefasst.
}
