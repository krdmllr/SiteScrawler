package de.sitescrawler.formatierung;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.model.ProjectConfig;

/**
 * @author Yvette FormatiererService wandelt den Archiveintrag in reinen Text, HTML-Format oder eine PDF-Datei um.
 */
@ApplicationScoped
public class FormatiererService implements IFormatiererService
{
    // Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    private HTMLHelfer          htmlHelfer;
    @Inject
    private PDFHelfer           pdfHelfer;
    @Inject
    private ProjectConfig       projectConfig;

    /**
     * Wandelt den Archiveintrag in einen reinen Text-String um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     */
    @Override
    public String generierePlaintextZusammenfassung(Archiveintrag archiveintrag)
    {
        Set<Artikel> artikelAusArchiveintrag = archiveintrag.getArtikel();
        String plainTextAusgabe = "";

        // Konkatiniert alle Daten zu den Artikel aus dem Archiveintrag zusammen
        if (!artikelAusArchiveintrag.isEmpty())
        {
            for (Artikel artikel : artikelAusArchiveintrag)
            {
                plainTextAusgabe += artikel.getTitel() + "\n" + artikel.getAutor() + "\n" + this.wandleDatumUm(artikel.getErstellungsdatum()) + "\n"
                                    + artikel.getBeschreibung() + "\n" + artikel.getLink() + "\n\n";
            }
            FormatiererService.LOGGER.log(Level.INFO, "Umwandlung des Archiveintrags in Plain-Text erfolgreich.");
        }
        else
        {
            plainTextAusgabe = "Leider befinden sich in diesem Archiveintrag keine Artikel.";
        }

        return plainTextAusgabe;
    }

    /**
     * Wandelt den Archiveintrag in HTML-Format um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag in HTML-Format als String.
     */
    @Override
    public String generiereHtmlZusammenfassung(Archiveintrag archiveintrag)
    {
        String htmlAusgabe = "";
        Set<Artikel> artikelAusArchiveintrag = archiveintrag.getArtikel();

        // Wandelt alle Artikel aus dem Archiveintrag in eine HTML-Datei um
        if (!artikelAusArchiveintrag.isEmpty())
        {

            htmlAusgabe = this.htmlHelfer.archiveintragInHTML(archiveintrag);

            FormatiererService.LOGGER.log(Level.INFO, "Umwandlung des Archiveintrags in HTML erfolgreich.");
        }
        else
        {
            htmlAusgabe = htmlHelfer.leerenArchiveintragInHtml();
            FormatiererService.LOGGER.log(Level.INFO, "Umwandlung des leeren Archiveintrags in HTML erfolgreich.");
        }

        return htmlAusgabe;
    }

    /**
     * Wandelt den Archiveintrag in eine PDF-Datei als ByteArrayDataSource um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag als ByteArrayDataSource.
     */
    @Override
    public ByteArrayDataSource generierePdfZusammenfassung(Archiveintrag archiveintrag)
    {
        ByteArrayOutputStream pdfOut = generierePDFAlsByteArrayOutputStream(archiveintrag);
        ByteArrayInputStream pdfIn = new ByteArrayInputStream(pdfOut.toByteArray());
        ByteArrayDataSource daten = null;

        try
        {
            daten = new ByteArrayDataSource(pdfIn, "application/pdf");
            daten.setName("Pressespiegel_" + archiveintrag.getErstellungsdatumFormatiert() + ".pdf");
            FormatiererService.LOGGER.log(Level.INFO, "Umwandlung des Archiveintrags von PDF in ByteArrayDataSource erfolgreich.");
        }
        catch (IOException e)
        {
            FormatiererService.LOGGER.log(Level.SEVERE, "Fehler beim Umwandeln des Archiveintrags von ByteArrayOutputStream zu ByteArrayDataSource", e);
        }

        return daten;
    }

    /**
     * Wandelt den Archiveintrag in eine PDF-Datei als ByteArrayOutputStream um.
     * 
     * @param archiveintrag
     * @return Umgewandelter Archiveintrag als ByteArrayOutputStream.
     */
    private ByteArrayOutputStream generierePDFAlsByteArrayOutputStream(Archiveintrag archiveintrag)
    {
        ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
        String aktuellesDatum = this.wandleDatumUm(new Date());

        Map<String, String> parameterFuerPDF = this.erstelleParameter(aktuellesDatum);

        try
        {
            // Falls Archiveintrag leer ist: XML nur mit <archiveintrag/> -> PDF-Generierung geht
            xmlOutputStream = this.pdfHelfer.schreibeArtikelAlsXML(archiveintrag);
            FormatiererService.LOGGER.log(Level.INFO, "Umwandlung des Archiveintrags in XML erfolgreich.");
        }
        catch (JAXBException e2)
        {
            FormatiererService.LOGGER.log(Level.SEVERE, "Fehler bei Umwandlung des Archiveintrags in XML.", e2);
        }

        try
        {
            pdfOut = this.wandleXMLinPDF(xmlOutputStream, pdfOut, parameterFuerPDF);
            FormatiererService.LOGGER.log(Level.INFO, "Umwandlung des Archiveintrags von XML in PDF erfolgreich.");
        }
        catch (IOException e1)
        {
            FormatiererService.LOGGER.log(Level.SEVERE, "Fehler bei Umwandlung des Archiveintrags von XML in PDF.", e1);
        }
        return pdfOut;
    }

    /**
     * Wandelt den Archiveintrag in eine PDF-Datei als ByteArrayOutputStream um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag als ByteArrayOutputStream.
     */
    @Override
    public ByteArrayOutputStream generierePdfZusammenfassungStream(Archiveintrag archiveintrag)
    {
        return generierePDFAlsByteArrayOutputStream(archiveintrag);
    }

    /**
     * Wandelt das übergebene Datum in eine leserliche Form um.
     *
     * @param datumUmzuwandeln
     * @return
     */
    public String wandleDatumUm(Date datumUmzuwandeln)
    {
        DateFormat df;

        df = DateFormat.getDateInstance(DateFormat.LONG, Locale.GERMAN);
        String aktuellesDatum = df.format(datumUmzuwandeln);

        return aktuellesDatum;
    }

    /**
     * Setzt die Parameter für die Umwandlung von Archiveintrag in PDF.
     * 
     * @param aktuellesDatum
     * @return
     */
    private Map<String, String> erstelleParameter(String aktuellesDatum)
    {
        Map<String, String> parameterFuerPDF = new HashMap<String, String>()
        {

            private static final long serialVersionUID = 1L;

            {
                String ressourcenDomain = FormatiererService.this.projectConfig.getRessourcenDomain();
                this.put("titelPDF", "Ihr persönlicher Pressespiegel von SiteScrawler!");
                this.put("aktuellesDatum", aktuellesDatum);
                this.put("footerText", "Dieser Pressespiegel wurde von SiteScrawler erstellt und versandt.");
                this.put("linkZuSiteScrawler", "https://sitescrawler.de");
            }
        };
        return parameterFuerPDF;
    }

    /**
     * Wandelt eine XML-Datei in eine PDF-Datei um.
     *
     * @param pdfHelfer
     * @param xmlDatei
     * @param pdf
     * @param parameterFuerPDF
     * @return
     * @throws IOException
     */
    private ByteArrayOutputStream wandleXMLinPDF(ByteArrayOutputStream xmlDatei, ByteArrayOutputStream pdf, Map<String, String> parameterFuerPDF)
        throws IOException
    {
        try
        {
            pdf = this.pdfHelfer.XMLzuPDF(xmlDatei, parameterFuerPDF);
            FormatiererService.LOGGER.info("Erfolgreiche Umwandlung von XML in PDF!");
        }
        catch (FileNotFoundException | FOPException | TransformerException e1)
        {
            FormatiererService.LOGGER.log(Level.SEVERE, "Fehler beim Umwandeln der XML-Datei in eine PDF-Datei!", e1);
        }
        finally
        {
            pdf.close();
        }
        return pdf;
    }

}