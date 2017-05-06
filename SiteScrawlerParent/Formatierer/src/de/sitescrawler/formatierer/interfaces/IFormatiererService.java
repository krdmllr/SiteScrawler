package de.sitescrawler.formatierer.interfaces;

import de.sitescrawler.jpa.Archiveintrag;

public interface IFormatiererService {

	String generierePlaintextZusammenfassung(Archiveintrag archiveintrag);
	
	String generiereHtmlZusammenfassung(Archiveintrag archiveintrag);
}
