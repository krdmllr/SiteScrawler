package de.sitescrawler.firmenverwaltung;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import de.sitescrawler.email.ServiceUnavailableException;
import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.firmenverwaltung.interfaces.IFirmenService;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;

public class FirmenService implements IFirmenService{

	// Globalen Logger holen
	private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

	
	@Inject
	private INutzerService nutzerService;
	
	@Inject 
	private IMailSenderService mailService;
	
	@Override
	public boolean IsFirmennameVerfuegbar(String name) {
		// TODO Marcel
		return true;
	}

	@Override
	public boolean FirmaBeantragen(Nutzer nutzer, String firmenName, String firmenMail, String kommentar) {
		if(!IsFirmennameVerfuegbar(firmenMail)) return false;
		
		Firma firma = new Firma(firmenName);
		//TODO Set firmen Email
		//TODO Set Status auf Beantragt
		//TODO Speichere neue Firma in Datenbank
		
		//Sende Information an alle Administratoren
		InformiereAdministratorenUeberNeueFirma(firma, nutzer, firmenMail);
		
		return true;
	}
	
	private void InformiereAdministratorenUeberNeueFirma(Firma firma, Nutzer nutzer, String kommentar){
		List<Nutzer> alleAdministratoren = nutzerService.getAlleAdministratoren();
		String betreff = "Neue Firma beantragt";
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("Nutzer " + nutzer.getGanzenNamen() + " hat die Firma " + firma.getName() + " beantragt.");
		bodyBuilder.append("");
		
		if(kommentar != null && !kommentar.isEmpty()){
			bodyBuilder.append("Der Nutzer hat folgenden Kommentar angegeben:");
			bodyBuilder.append(kommentar);
		} 
		
		try {
			mailService.sendeMail(alleAdministratoren, betreff , bodyBuilder.toString(), false, null);
		} catch (ServiceUnavailableException e) {
			LOGGER.log(Level.SEVERE, "Administratoren konnte nicht über neuen Firmenantrag informiert werden", e);
		}
	}

	@Override
	public void NutzerEinladen(Firma firma, String email, String vorname, String nachname, Nutzer angemeldeterNutzer) throws ServiceUnavailableException {
		 Nutzer neuerNutzer = new Nutzer(email, vorname, nachname);
		 neuerNutzer.getFirmen().add(firma);
		 nutzerService.registrieren(neuerNutzer, firma);
	}

	@Override
	public void NutzerEntfernen(Firma firma, Nutzer zuEntfernenderNutzer, Nutzer angemeldeterNutzer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetzeNutzerRolle(Firma firma, Nutzer nutzer, Rolle rolle, Nutzer angemeldeterNutzer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loescheFirma(Firma firma, Nutzer angemeldeterNutzer) {
		// TODO Auto-generated method stub
		
	}

}
