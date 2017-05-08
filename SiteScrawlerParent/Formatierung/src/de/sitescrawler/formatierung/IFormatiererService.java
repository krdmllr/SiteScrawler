package de.sitescrawler.formatierung;

import de.sitescrawler.jpa.Archiveintrag;

public interface IFormatiererService
{

    String generierePlaintextZusammenfassung(Archiveintrag archiveintrag);

    String generiereHtmlZusammenfassung(Archiveintrag archiveintrag);
}
