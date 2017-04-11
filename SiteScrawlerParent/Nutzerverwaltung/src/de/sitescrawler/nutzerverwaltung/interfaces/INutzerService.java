package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

/**
 * Service, der Nutzerdaten bereitstellt und deren Rolle verändert.
 * @author konrad mueller
 *
 */
public interface INutzerService {
	
	/**
	 * Brauchen wir mmn nicht
	 * @param nutzer
	 */
	void nutzerSpeichern(Nutzer nutzer);

	/**
	 * Warum sollten wir zur Laufzeit rollen anlegen?
	 * @param rolle
	 */
	void rolleAnlegen(Rolle rolle);

	/**
	 * Gibt einen Nutzer anhand seiner id zurück.	
	 * @param uid Id des Nutzers.
	 * @return Nutzer mit der angegebenen Id.
	 */
	Nutzer getNutzer(String uid);
}
