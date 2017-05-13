package de.sitescrawler.formatierung;

import java.io.ByteArrayOutputStream;

import javax.mail.util.ByteArrayDataSource;

import de.sitescrawler.jpa.Archiveintrag;

public interface IFormatiererService
{

    String generierePlaintextZusammenfassung(Archiveintrag archiveintrag);

    String generiereHtmlZusammenfassung(Archiveintrag archiveintrag);

    // TODO PDF-Generierung
    ByteArrayDataSource generierePdfZusammenfassung(Archiveintrag archiveintrag);
    
    ByteArrayOutputStream generierePdfZusammenfassungStream(Archiveintrag archiveintrag);
}
