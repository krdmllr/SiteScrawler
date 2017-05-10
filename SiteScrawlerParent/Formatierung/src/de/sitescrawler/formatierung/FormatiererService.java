package de.sitescrawler.formatierung;

import java.io.File;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;

/**
 * @author Yvette
 * FormatiererService wandelt den Archiveintrag in reinen Text, HTML-Format oder eine PDF-Datei um.
 */
@ApplicationScoped
public class FormatiererService implements IFormatiererService
{
    /**
     * Wandelt den Archiveintrag in einen reinen Text-String um.
     *
     * @param archiveintrag Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag als reinen Text-String.
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
                plainTextAusgabe += artikel.getTitel() + "\n"
                                + artikel.getAutor() + "\n"
                                + artikel.getErstellungsdatum() + "\n"
                                + artikel.getBeschreibung() + "\n"
                                + artikel.getLink() + "\n\n";
            }
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
     * @param archiveintrag Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag als HTML-Format.
     */
    @Override
    public String generiereHtmlZusammenfassung(Archiveintrag archiveintrag)
    {
        String htmlAusgabe = "";
        Set<Artikel> artikelAusArchiveintrag = archiveintrag.getArtikel();

        // Wandelt alle Artikel aus dem Archiveintrag in eine HTML-Datei um
        if (!artikelAusArchiveintrag.isEmpty())
        {
            HTMLHelfer htmlHelfer = new HTMLHelfer();
            htmlAusgabe = htmlHelfer.archiveintragInHTML(archiveintrag);
        }

        return htmlAusgabe;
    }

    /**
     * Wandelt den Archiveintrag in eine PDF-Datei um.
     *
     * @param archiveintrag Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag als PDF-Datei.
     */
    @Override
    public File generierePdfZusammenfassung(Archiveintrag archiveintrag)
    {
        // TODO Auto-generated method stub
        return null;
    }

}