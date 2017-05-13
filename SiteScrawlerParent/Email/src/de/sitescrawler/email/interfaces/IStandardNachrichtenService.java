package de.sitescrawler.email.interfaces;

import java.time.LocalDateTime;
import java.util.Date;

import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Nutzer;

public interface IStandardNachrichtenService {

	/**
	 * Sendet dem Nutzer eine Email nach dem er sich registriert hat und der Aufforderung, ein neues Paswort zu setzen.
	 * @param nutzer
	 * @return
	 * @throws ServiceUnavailableException 
	 */
	void registrierungsMail(Nutzer nutzer) throws ServiceUnavailableException;
	
	/**
	 * Sendet dem Nutzer eine Email nach dem er über eine Firma registriert wurde und der Aufforderung, ein neues Paswort zu setzen.
	 * @param nutzer
	 * @param firma
	 * @return
	 * @throws ServiceUnavailableException 
	 */
	void registrierungUeberFirma(Nutzer nutzer, Firma firma) throws ServiceUnavailableException;
	
	/**
	 * Sendet dem Nutzer eine Email mit der Information, dass ein neues Passwort für seinen Account gesetzt wurde.
	 * @param nutzer
	 * @param ruecksetzZeitangabe
	 * @return
	 */
	void neuesPasswortGesetzt(Nutzer nutzer, LocalDateTime ruecksetzZeitangabe);
	
	/**
	 * Sendet dem Nutzer eine Email mit einem Temporären Passwort und der Auffoderung es zu ändern.
	 * @param nutzer
	 * @param temporaeresPasswort
	 * @return
	 * @throws ServiceUnavailableException 
	 */
	void passwortZuruecksetzen(Nutzer nutzer, String temporaeresPasswort) throws ServiceUnavailableException;

	/**
	 * Informiere den Nutzer, dass er als Mitarbeiter von einer Firma entfernt wurde.
	 * @param nutzer
	 * @param firma
	 */
	void vonFirmaEntfernt(Nutzer nutzer, Firma firma, Nutzer ausfuehrenderNutzer);
	
	/**
	 * Informiert den Nutzer, dass er einer Firma als Mitarbeiter hinzugefuegt wurde.
	 * @param nutzer
	 * @param firma
	 * @param ausfuehrenderNutzer
	 */
	void zuFirmaHinzugefuegt(Nutzer nutzer, Firma firma, Nutzer ausfuehrenderNutzer);
	
	/**
	 * Informiert alle Nutzer der Firma, dass die Firma gelöscht wurde.
	 * @param firma
	 */
	void firmaGeloescht(Firma firma);
}
