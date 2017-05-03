package de.sitescrawler.email.interfaces;

import java.util.List;

import de.sitescrawler.jpa.Nutzer; 

/**
 * Verwaltet das Senden von Emails.
 * @author konrad mueller
 */
public interface IMailSenderService {

	/**
	 * Sendet eine Email an die angegebene Email Adresse.
	 * @param emailAdresse Adresse des Empfängers.
	 * @param subjekt Email Betreff.
	 * @param body Email Inhalt.
	 * @param htmlBody Soll der Email Inhalt als HTML gesendet werden.
	 * @param anhaenge Eine Liste von Anhängen.
	 */
	void sendeMail(String emailAdresse, String subjekt, String body, boolean htmlBody,  List<byte []> anhaenge);
	
	/**
	 * Sendet eine Email an eine Liste von Empfängern.
	 * @param empfaenger E-Mail Adressen der Empfänger.
	 * @param subjekt Email Betreff.
	 * @param body Email Inhalt.
	 * @param htmlBody Soll der Email Inhalt als HTML gesendet werden.
	 * @param pdf Anzuhängendes PDF.
	 */
	void sendeMassenMail(List<Nutzer> empfaenger, String subjekt, String body, boolean htmlBody, byte[] pdf);
}
