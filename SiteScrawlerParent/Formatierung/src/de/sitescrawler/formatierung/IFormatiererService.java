package de.sitescrawler.formatierung;

import java.io.ByteArrayOutputStream;

import javax.mail.util.ByteArrayDataSource;

import de.sitescrawler.jpa.Archiveintrag;

public interface IFormatiererService
{
    /**
     * Wandelt den Archiveintrag in einen reinen Text-String um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     */
    String generierePlaintextZusammenfassung(Archiveintrag archiveintrag);

    /**
     * Wandelt den Archiveintrag in HTML-Format um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag in HTML-Format als String.
     */
    String generiereHtmlZusammenfassung(Archiveintrag archiveintrag);

    /**
     * Wandelt den Archiveintrag in eine PDF-Datei als ByteArrayDataSource um.
     *
     * @param archiveintrag
     *            Der Archiveintrag, der umgewandelt werden soll.
     * @return Umgewandelter Archiveintrag als ByteArrayDataSource.
     */
    ByteArrayDataSource generierePdfZusammenfassung(Archiveintrag archiveintrag);

    /**
     * Wandelt den Archiveintrag in eine PDF-Datei als ByteArrayOutputStream um.
     * 
     * @param archiveintrag
     * @return Umgewandelter Archiveintrag als ByteArrayOutputStream.
     */
    ByteArrayOutputStream generierePdfZusammenfassungStream(Archiveintrag archiveintrag);
}
