package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;

/**
 * Service, welcher die Registrierung von Nutzern verwaltet.
 * @author konrad mueller
 *
 */
public interface INutzerRegistrierungsService {

	/**
	 * Gibt zurück, ob eine Email bereits in verfügbar ist.
	 * @return ob die Email bereits in Verwendung ist.
	 */
	boolean isEmailVerfuegbar(String email);
	
	/**
	 * Registriert einen neuen Nutzer, schickt dem Nutzer einen Bestätigungslink zu.
	 * @param nutzer Anzulegender Nutzer.
	 */
	void registrieren(Nutzer nutzer);
}
