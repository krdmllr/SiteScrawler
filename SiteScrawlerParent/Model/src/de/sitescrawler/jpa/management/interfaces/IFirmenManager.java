package de.sitescrawler.jpa.management.interfaces;

import de.sitescrawler.jpa.Firma;

public interface IFirmenManager {

	/**
	 * Speichert die Änderungen an der Firma und allen Unterelementen.
	 * @param firma
	 */
	void speichereAenderungen(Firma firma);
}
