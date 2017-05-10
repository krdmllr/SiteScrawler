package de.sitescrawler.jpa.management.interfaces;

import de.sitescrawler.jpa.Filterprofilgruppe;

public interface IFiltergruppenManager {
	
	/**
	 * Speichert alle in der Filtergruppe vorgenommenen Änderungen.
	 * @param filtergruppe
	 */
	void speichereAenderung(Filterprofilgruppe filtergruppe);
}
