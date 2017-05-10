package de.sitescrawler.formatierung;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;

/**
 * @author Yvette
 * Stellt die Hilfsmethoden zur Verfügung, um den Archiveintrag in eine PDF-Datei umzuwandeln.
 */
public class PDFHelfer
{
    // Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");
    File pdffile = new File("Result.pdf");
    File xsltDatei = new File("src/de/sitescrawler/hilfsdateien/xmlZuPdf.xsl");


    /**
     * Wandelt den Archiveintrag in eine XML-Datei um.
     *
     * @param archiveintrag
     * @param xmlDatei
     * @throws JAXBException
     */
    public void schreibeArtikelAlsXML(Archiveintrag archiveintrag, File xmlDatei) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(Archiveintrag.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        archiveintrag = this.datumFormatieren(archiveintrag);

        m.marshal(archiveintrag, xmlDatei);
    }

    private Archiveintrag datumFormatieren(Archiveintrag archiveintrag)
    {
        // TODO Datum umformatieren in XML
        SimpleDateFormat formatter = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMAN);
        Set<Artikel> artikelListe = archiveintrag.getArtikel();

        for (Artikel art : artikelListe)
        {
            Date datumAlt = art.getErstellungsdatum();
            String datumNeu = formatter.format(datumAlt);

            try
            {
                Date datum = formatter.parse(datumNeu);
                art.setErstellungsdatum(datum);
            }
            catch (ParseException e)
            {
                PDFHelfer.LOGGER.log(Level.SEVERE, "Umwandlung des Datums für XML fehlgeschlagen!", e);
            }
        }

        return archiveintrag;
    }

    private static Transformer erstelleTransformer(File xlst, Map<String, String> parameter) throws TransformerConfigurationException
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xlst));
        parameter.forEach(transformer::setParameter);
        return transformer;
    }

    /**
     * Wandelt die XML mit Hilfe der XSL in eine PDF-Datei.
     *
     * @param xmlZuTransformieren
     * @param parameter
     * @return PDF-Datei als ByteArrayOutputStream.
     * @throws FileNotFoundException
     * @throws FOPException
     * @throws TransformerException
     */
    public ByteArrayOutputStream XMLzuPDF(File xmlZuTransformieren, Map<String, String> parameter) throws FileNotFoundException, FOPException, TransformerException
    {

        // FopFactory konfigurieren
        final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        // Output konfigurieren
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();

        // FOP mit gewünschter Ausgabe konfigurieren
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfOut);

        // XSLT vorbereiten
        Transformer transformer = PDFHelfer.erstelleTransformer(this.xsltDatei, parameter);

        // XML als Input für die Transformation setzen
        Source eingabeZuKonvertieren = new StreamSource(xmlZuTransformieren);

        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result ausgabe = new SAXResult(fop.getDefaultHandler());

        // XSLT-Transformation beginnen
        transformer.transform(eingabeZuKonvertieren, ausgabe);

        try
        {
            pdfOut.close();
        }
        catch (IOException e)
        {
            PDFHelfer.LOGGER.log(Level.SEVERE, "Fehler beim Schließen der PDF-Datei!", e);
        }

        return pdfOut;
    }
}
