package de.sitescrawler.formatierung;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
import de.sitescrawler.model.ProjectConfig;

/**
 * @author Yvette Stellt die Hilfsmethoden zur Verfügung, um den Archiveintrag in eine PDF-Datei umzuwandeln.
 */
@ApplicationScoped
public class PDFHelfer
{
    // Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");
    private File                xsltDatei;
    @Inject
    private ProjectConfig       projectConfig;

    public PDFHelfer()
    {
    }

    /**
     * Wird direkt nach dem Contruktor aufgerufen um auf die benötigte XSLT-Datei zugreifen zu können.
     */
    @PostConstruct
    private void init()
    {
        String ressourcenDomain = this.projectConfig.getRessourcenDomain();
        this.xsltDatei = new File(ressourcenDomain + "/xmlZuPdf.xsl");
    }

    /**
     * Wandelt den Archiveintrag in eine XML-Datei um.
     *
     * @param archiveintrag
     * @param xmlDatei
     * @throws JAXBException
     */
    public ByteArrayOutputStream schreibeArtikelAlsXML(Archiveintrag archiveintrag) throws JAXBException
    {
        ByteArrayOutputStream xmlDatei = new ByteArrayOutputStream();
        JAXBContext jc = JAXBContext.newInstance(Archiveintrag.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(archiveintrag, xmlDatei);
        return xmlDatei;
    }

    /**
     * Erstellt den Transformator mit dem der Archiveintrag durch die XSLT-Datei zu einer PDF-Datei konvertiert werden
     * kann.
     * 
     * @param xslt
     * @param parameter
     * @return
     * @throws TransformerConfigurationException
     */
    private static Transformer erstelleTransformer(File xslt, Map<String, String> parameter) throws TransformerConfigurationException
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource streamSource = new StreamSource(xslt);
        Transformer transformer = factory.newTransformer(streamSource);
        parameter.forEach(transformer::setParameter);
        return transformer;
    }

    /**
     * Wandelt die XML mit Hilfe der XSLT in eine PDF-Datei.
     *
     * @param xmlZuTransformieren
     * @param parameter
     * @return PDF-Datei als ByteArrayOutputStream.
     * @throws FileNotFoundException
     * @throws FOPException
     * @throws TransformerException
     */
    public ByteArrayOutputStream XMLzuPDF(ByteArrayOutputStream xmlZuTransformieren, Map<String, String> parameter)
        throws FileNotFoundException, FOPException, TransformerException
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
        ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xmlZuTransformieren.toByteArray());
        Source eingabeZuKonvertieren = new StreamSource(xmlInputStream);

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
