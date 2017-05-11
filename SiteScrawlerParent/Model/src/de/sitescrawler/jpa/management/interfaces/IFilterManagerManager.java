package de.sitescrawler.jpa.management.interfaces;

import de.sitescrawler.jpa.Filtermanager;

public interface IFilterManagerManager {

	/**
	 * Übernimmt die innerhalb des Filtermanagers vorgenommenen Änderungen.
	 */
	void speichereAenderung(Filtermanager filterManager);
}
