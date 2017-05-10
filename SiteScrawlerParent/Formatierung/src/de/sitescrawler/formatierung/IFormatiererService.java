package de.sitescrawler.formatierung;

import java.io.File;

import de.sitescrawler.jpa.Archiveintrag;

public interface IFormatiererService
{

    String generierePlaintextZusammenfassung(Archiveintrag archiveintrag);

    String generiereHtmlZusammenfassung(Archiveintrag archiveintrag);
    
    // TODO PDF-Generierung
    File generierePdfZusammenfassung(Archiveintrag archiveintrag);
}
