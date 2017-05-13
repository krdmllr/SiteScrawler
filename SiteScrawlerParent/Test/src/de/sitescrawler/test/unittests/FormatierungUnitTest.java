package de.sitescrawler.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.mail.util.ByteArrayDataSource;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.sitescrawler.formatierung.FormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.test.testdaten.FormatierungTestdaten;

public class FormatierungUnitTest
{
    static String        plainTextErwartet = "";
    static String        htmlErwartet      = "";
    static Archiveintrag eintrag           = null;

    @BeforeClass
    public static void setUp()
    {
        FormatierungTestdaten td = new FormatierungTestdaten();

        plainTextErwartet = td.plainText;
        htmlErwartet = td.htmlString;
        eintrag = td.archiveintrag;
    }

    @Test
    public void sollPlainTextGenerieren()
    {
        String plainTextTest = "";

        FormatiererService fs = new FormatiererService();

        plainTextTest = fs.generierePlaintextZusammenfassung(eintrag);

        assertEquals("Plain-Texte sind gleich.", plainTextErwartet, plainTextTest);
    }

    @Ignore
    // Problem: Pfad der Hilfsdateien
    public void sollHTMLGenerieren()
    {
        String htmlTextTest = "";

        FormatiererService fs = new FormatiererService();

        htmlTextTest = fs.generiereHtmlZusammenfassung(eintrag);

        assertEquals("HTML-Strings sind gleich.", htmlErwartet, htmlTextTest);
    }

    @Ignore
    // Problem: Pfad der Hilfsdateien
    public void sollPDFGenerieren()
    {
        ByteArrayDataSource pdfTest = null;

        FormatiererService fs = new FormatiererService();

        pdfTest = fs.generierePdfZusammenfassung(eintrag);

        assertNotNull(pdfTest);
    }
}
