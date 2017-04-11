package de.sitescrawler.email.interfaces;

import java.util.List;

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
	 * @param emailAdressen E-Mail Adressen der Empfänger.
	 * @param subjekt Email Betreff.
	 * @param body Email Inhalt.
	 * @param htmlBody Soll der Email Inhalt als HTML gesendet werden.
	 * @param anhaenge Eine Liste von Anhängen.
	 */
	void sendeMassenMail(List<String> emailAdressen, String subjekt, String body, boolean htmlBody, List<byte []> anhaenge);
}
