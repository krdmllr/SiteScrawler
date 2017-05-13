package de.sitescrawler.formatierung;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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

    @PostConstruct
    private void init()
    {
        String ressourcenDomain = this.projectConfig.getRessourcenDomain();
        this.xsltDatei = new File(ressourcenDomain + "/xmlZuPdf.xsl");
        try (FileReader fr = new FileReader(this.xsltDatei))
        {
            BufferedReader bufferedReader = new BufferedReader(fr);
            bufferedReader.lines().forEach(System.out::println);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
        m.marshal(archiveintrag, xmlDatei);
    }

    private static Transformer erstelleTransformer(File xslt, Map<String, String> parameter) throws TransformerConfigurationException
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        try (FileReader fr = new FileReader(xslt))
        {
            BufferedReader bufferedReader = new BufferedReader(fr);
            bufferedReader.lines().forEach(System.out::println);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StreamSource streamSource = new StreamSource(xslt);
        // TODO remove
        Reader reader = streamSource.getReader();
        BufferedReader bufferedReader = new BufferedReader(reader);
        bufferedReader.lines().forEach(System.out::println);
        Transformer transformer = factory.newTransformer(streamSource);
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
    public ByteArrayOutputStream XMLzuPDF(File xmlZuTransformieren, Map<String, String> parameter)
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
