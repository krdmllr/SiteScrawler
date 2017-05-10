package de.sitescrawler.formatierung;

import java.io.File;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

public class PDFHelfer
{
    // Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");
    File pdffile = new File("Result.pdf");

    public File XMLzuPDF(File xmlZuTransformieren)
    {
        try
        {
            // XSLT-Datei zur Umwandlung definieren
            File xsltfile = new File("src/de/sitescrawler/hilfsdateien/xmlZuPdf.xsl");

            // FopFactory konfigurieren
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            // FOUserAgent konfigurieren
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            // Output konfigurieren
            OutputStream out = new java.io.FileOutputStream(this.pdffile);
            out = new java.io.BufferedOutputStream(out);

            try {
                // FOP mit gewünschter Ausgabe konfigurieren
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // XSLT vorbereiten
                TransformerFactory factory = TransformerFactory.newInstance();
                // TODO: XSLT-Datei
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                // Parameter in PDF füllen
                // TODO: Parameter setzen
                transformer.setParameter("versionParam", "2.0");

                // XML als Input f�r die Transformation setzen
                Source eingabeZuKonvertieren = new StreamSource(xmlZuTransformieren);

                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // XSLT-Transformation beginnen
                transformer.transform(eingabeZuKonvertieren, res);

                PDFHelfer.LOGGER.info("Transformiere XML in PDF...");
            }
            finally
            {
                // Output-Stream schließen
                out.close();
            }

            PDFHelfer.LOGGER.info("XML erfolgreich transformiert!");
        }
        catch (Exception e)
        {
            PDFHelfer.LOGGER.log(Level.SEVERE, "Fehler bei der Transformation von XML zu PDF!", e);
        }

        return this.pdffile;
    }
}
