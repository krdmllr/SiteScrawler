package de.sitescrawler.pdf.interfaces;

import de.sitescrawler.jpa.Archiveintrag;

/**
 * Service zum erstellen von PDF Dateien.
 * @author konrad mueller
 */
public interface IPdfErstellungsService {

	/**
	 * Erstellt ein PDF, dass einen Archiveintrag abbildet.
	 * @param archiveintrag Archiveintrag, welcher als PDF exportiert werden soll.
	 * @return Das erstellte PDF als byte array.
	 */
	byte [] erstellePdf(Archiveintrag archiveintrag);
}
