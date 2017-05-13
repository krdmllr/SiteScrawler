package de.sitescrawler.email;

import java.time.LocalDateTime;

import javax.inject.Inject;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.email.interfaces.IStandardNachrichtenService;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Nutzer;

public class StandardNachrichtenService implements IStandardNachrichtenService{

	private final String BITTE_PASSWORT_SETZEN = "Ihnen wurde ein temporäres Passwort zugeteilt. Bitte ändern Sie dieses nach Ihrer ersten Anmeldung.";
	private final String PASSWORT_TEXT ="Das Temporäre Passwort lautet:  ";
	
	@Inject
	private IMailSenderService mailSenderService;
	
	@Override
	public void registrierungsMail(Nutzer nutzer) throws ServiceUnavailableException {
		String betreff = "Ihr neues Konto bei sitescrawler.de";
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("Herzlich willkommen " + nutzer.getGanzenNamen());
		bodyBuilder.append("Ihr Konto wurde erfolgreich angelegt.");
		bodyBuilder.append("");
		bodyBuilder.append(BITTE_PASSWORT_SETZEN);
		bodyBuilder.append(PASSWORT_TEXT + nutzer.getPasswort());
		bodyBuilder.append("");
		bodyBuilder.append("- SiteScrawler Team"); 
		String body = bodyBuilder.toString();
		
		mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
	}

	@Override
	public void registrierungUeberFirma(Nutzer nutzer, Firma firma) throws ServiceUnavailableException {
		String betreff = "Ihr neues Konto bei sitescrawler.de";
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("Herzlich willkommen " + nutzer.getGanzenNamen());
		bodyBuilder.append("Das Unternhemen " + firma.getName() + " hat für Sie einen Accoutn angelegt.");
		bodyBuilder.append("");
		bodyBuilder.append(BITTE_PASSWORT_SETZEN);
		bodyBuilder.append(PASSWORT_TEXT + nutzer.getPasswort());
		bodyBuilder.append("");
		bodyBuilder.append("- SiteScrawler Team"); 
		String body = bodyBuilder.toString();
		
		mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
		
	}

	@Override
	public void neuesPasswortGesetzt(Nutzer nutzer, LocalDateTime ruecksetzZeitangabe) {
		String betreff = "Passwort auf sitescrawler.de geändert";
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("Hallo " + nutzer.getGanzenNamen());
		bodyBuilder.append("Wir senden Ihnen diese Nachricht um Sie darüber zu informieren, dass Ihr Passwort auf sitescrawler.de geändert wurde.");
		//TODO Schreibe was man machen soll, wenn man das net selbst war.
		bodyBuilder.append("- SiteScrawler Team"); 
		String body = bodyBuilder.toString();
		
		try {
			mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
		} catch (ServiceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void passwortZuruecksetzen(Nutzer nutzer, String temporaeresPasswort) throws ServiceUnavailableException {
		String betreff = "Ihr Passwort auf sitescrawler.de wurde zurückgesetzt.";
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("Hallo " + nutzer.getGanzenNamen()); 
		bodyBuilder.append("");
		bodyBuilder.append(BITTE_PASSWORT_SETZEN);
		bodyBuilder.append(PASSWORT_TEXT + nutzer.getPasswort());
		bodyBuilder.append("");
		bodyBuilder.append("- SiteScrawler Team"); 
		String body = bodyBuilder.toString();
		
		mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
		
	}

}
