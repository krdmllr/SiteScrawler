package de.sitescrawler.test.unittests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.sitescrawler.services.artikelausschneiden.ArtikelZurechtschneiden;
import de.sitescrawler.services.artikelausschneiden.UnparsbarException;
import de.sitescrawler.test.testdaten.CrawlerTestdaten;
import junit.framework.Assert;

public class CrawlerUnitTest
{

    @Test
    public void f10_1ZuschneidenSollteKorrekteAbsaetzeLiefern()
    {
        List<String> spiegel_WieGelaehmt_Absaetze_erwartet = CrawlerTestdaten.spiegel_WieGelaehmt_Absaetze;
        String spiegel_WieGelaehmt_ArtikelUrl_WieGelaehmt_erwartet = CrawlerTestdaten.spiegel_WieGelaehmt_ArtikelUrl_WieGelaehmt;

        List<String> spiegel_WieGelaehmt_Absaetze_ausgeschnitten = new ArrayList<>();
        try
        {
            // TODO String aus Datenbank lesen
            spiegel_WieGelaehmt_Absaetze_ausgeschnitten = new ArtikelZurechtschneiden().getAbsaetze(spiegel_WieGelaehmt_ArtikelUrl_WieGelaehmt_erwartet,
                            "spArticleContent");
        }
        catch (UnparsbarException e)
        {
            Assert.fail();
        }

        Assert.assertTrue(spiegel_WieGelaehmt_Absaetze_ausgeschnitten.equals(spiegel_WieGelaehmt_Absaetze_erwartet));
    }

}
