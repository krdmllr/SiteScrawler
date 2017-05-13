package de.sitescrawler.formatierung;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.model.ProjectConfig;

/**
 * @author Yvette Stellt die Hilfsmethoden zur Verfügung, um den Archiveintrag in einen String im HTML-Format
 *         umzuwandeln.
 */
@ApplicationScoped
public class HTMLHelfer
{
    // Globalen Logger holen
    private final static Logger LOGGER      = Logger.getLogger("de.sitescrawler.logger");

    private String              htmlVorher  = "";
    private String              htmlNachher = "";
    private String              htmlGanz    = "";
    @Inject
    private ProjectConfig       projectConfig;

    public HTMLHelfer()
    {
    }

    @PostConstruct
    private void init()
    {
        String ressourcenDomain = this.projectConfig.getRessourcenDomain();
        // HTML-Datei vor den Artikeln (style, head, header)
        File htmlVorherFile = new File(ressourcenDomain + "/HTMLvorArtikel.html");
        this.htmlVorher = this.konvertiereHTMLDateiInString(htmlVorherFile);
        // HTML-Datei nach den Artikel (footer, end-Tags)
        File htmlNachherFile = new File(ressourcenDomain + "/HTMLnachArtikel.html");
        this.htmlNachher = this.konvertiereHTMLDateiInString(htmlNachherFile);
    }

    private String konvertiereHTMLDateiInString(File htmlZumKonvertieren)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(htmlZumKonvertieren), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null)
            {
                contentBuilder.append(str);
            }
            in.close();
        }
        catch (IOException e)
        {
            HTMLHelfer.LOGGER.log(Level.SEVERE, "Fehler beim Konvertieren der HTML-Datei!", e);
        }

        String inhalt = contentBuilder.toString();

        inhalt = inhalt.replaceAll("\t", " ");

        return inhalt;
    }

    private String konvertiereHTMLInString(String htmlZumKonvertieren)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try
        {
            InputStream stream = new ByteArrayInputStream(htmlZumKonvertieren.getBytes(StandardCharsets.UTF_8));
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String str;
            while ((str = in.readLine()) != null)
            {
                contentBuilder.append(str);
            }
            in.close();
            HTMLHelfer.LOGGER.log(Level.INFO, "Konvertieren in HTML erfolgreich.");
        }
        catch (IOException e)
        {
            HTMLHelfer.LOGGER.log(Level.SEVERE, "Fehler beim Konvertieren der HTML-Datei!", e);
        }

        String inhalt = contentBuilder.toString();

        inhalt = inhalt.replaceAll("\t", " ");

        return inhalt;
    }

    private String htmlVervollstaendigen(String artikelAlsHTMLString)
    {
        String html = "";

        // Allen HTML-Code vor den Artikeln einfügen
        html = this.htmlVorher;

        // Artikel in HTML-Format einfügen
        html += artikelAlsHTMLString;

        // Allen HTML-Code nach den Artikeln einfügen
        html += this.htmlNachher;

        return html;
    }

    public String leerenArchiveintragInHtml()
    {
        String htmlMeldungLeererArchiveintrag = "<div id=\"artikel\">" + "\n" + "<h3><b>" + "In diesem Archiveintrag sind keine Artikel enthalten."
                                                + "</b></h3>" + "\n" + "</div>";
        String htmlLeererArchiveintrag = "";

        htmlLeererArchiveintrag = this.htmlVervollstaendigen(htmlMeldungLeererArchiveintrag);

        return htmlLeererArchiveintrag;
    }

    /**
     * Wandelt den Archiveintrag in eine vollständige HTML-Datei um.
     *
     * @param archiveintrag
     *            Erwartet den umzuwandelnden Archiveintrag.
     * @return Gibt den Archiveintrag als vollstädnige HTML-Datei als String zurück.
     */
    public String archiveintragInHTML(Archiveintrag archiveintrag)
    {
        String artikelAlsHTMLString = "";
        Set<Artikel> artikelAusArchiveintrag = archiveintrag.getArtikel();
        FormatiererService fs = new FormatiererService();

        for (Artikel artikel : artikelAusArchiveintrag)
        {
            String aktuellesDatum = fs.wandleDatumUm(artikel.getErstellungsdatum());
            artikelAlsHTMLString += "<div id=\"artikel\">" + "\n" + "<h3><b>" + artikel.getTitel() + "</b></h3>" + "\n" + "<p id=\"datumUndAutor\">" + "Von "
                                    + artikel.getAutor() + " am " + aktuellesDatum + "</p>" + "\n" + "<p id=\"beschreibung\">" + artikel.getBeschreibung()
                                    + "</p>" + "\n" + "<a target=\"_blank\" href=\"" + artikel.getLink() + "\" class=\"button\">" + "Zum vollen Artikel"
                                    + "</a>" + "\n" + "</div>";
        }

        artikelAlsHTMLString = this.konvertiereHTMLInString(artikelAlsHTMLString);

        // Fügt den nötigen HTML-Code vor und nach den Artikeln (in HTML-Format) ein
        this.htmlGanz = this.htmlVervollstaendigen(artikelAlsHTMLString);

        return this.htmlGanz;
    }
}
