package de.sitescrawler.test.unittests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import junit.runner.Version;

public class BeispielUnitTest
{
    static int eins = 0;
    static int zwei = 0;
    static int drei = 0;

    @BeforeClass
    public static void setUp()
    {
        BeispielUnitTest.eins = 1;
    }

    @Test
    public void testFallEins()
    {
        BeispielUnitTest.zwei = 1;

        Assert.assertEquals("Eins und Zwei haben beide den Wert 1", BeispielUnitTest.eins, BeispielUnitTest.zwei);
    }

    @Test
    public void testFalleZwei()
    {
        int max = 1000000;
        for (int i = 0; i < max; i++)
        {
            BeispielUnitTest.drei++;
        }

        Assert.assertEquals("Beide sollen gleich sein", max, BeispielUnitTest.drei);
    }

    @Ignore
    @Test
    public void zeigeJUnitVersion()
    {
        System.out.println("JUnit version is: " + Version.id());
    }
}
