package de.sitescrawler.firmenverwaltung;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.email.interfaces.IStandardNachrichtenService;
import de.sitescrawler.exceptions.FirmenSecurityException;
import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.firmenverwaltung.interfaces.IFirmenService;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer; 
import de.sitescrawler.model.Firmenrolle;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;

@SessionScoped
public class FirmenService implements IFirmenService, Serializable {
  
	private static final long serialVersionUID = 1L;

	// Globalen Logger holen
	private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

	@Inject
	private INutzerService nutzerService;

	@Inject
	private IMailSenderService mailService;

	@Inject
	private IStandardNachrichtenService standardNachrichten;
	
	@Inject
	private INutzerDatenService nutzerDatenService;
	
	private Nutzer ausfuehrenderNutzer;
	
	public Nutzer getNutzer(){
		if(ausfuehrenderNutzer == null)
			ausfuehrenderNutzer = nutzerDatenService.getNutzer();
	
		return ausfuehrenderNutzer;
	}

	@Override
	public boolean istFirmenMailVerfuegbar(String name) {
		// TODO Marcel
		return true;
	}

	@Override
	public boolean FirmaBeantragen(String firmenName, String firmenMail, String kommentar) {
		if (!istFirmenMailVerfuegbar(firmenMail))
			return false;

		Firma firma = new Firma(firmenName);
		// TODO Set firmen Email
		// TODO Set Status auf Beantragt
		// TODO Speichere neue Firma in Datenbank

		// Sende Information an alle Administratoren
		InformiereAdministratorenUeberNeueFirma(firma, firmenMail);

		return true;
	}

	private void InformiereAdministratorenUeberNeueFirma(Firma firma, String kommentar) {
		List<Nutzer> alleAdministratoren = nutzerService.getAlleAdministratoren();
		String betreff = "Neue Firma beantragt";
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append("Nutzer " + getNutzer().getGanzenNamen() + " hat die Firma " + firma.getName() + " beantragt.");
		bodyBuilder.append("");

		if (kommentar != null && !kommentar.isEmpty()) {
			bodyBuilder.append("Der Nutzer hat folgenden Kommentar angegeben:");
			bodyBuilder.append(kommentar);
		}

		try {
			mailService.sendeMail(alleAdministratoren, betreff, bodyBuilder.toString(), false, null);
		} catch (ServiceUnavailableException e) {
			LOGGER.log(Level.SEVERE, "Administratoren konnte nicht über neuen Firmenantrag informiert werden", e);
		}
	}

	@Override
	public void nutzerEinladen(Firma firma, String email, String vorname, String nachname)
			throws ServiceUnavailableException, Exception {

		istNutzerBerechtigt(firma, Firmenrolle.Administrator);

		Nutzer neuerNutzer = new Nutzer(email, vorname, nachname);
		neuerNutzer.getFirmen().add(firma);
		Mitarbeiter neuerMitarbeiter = new Mitarbeiter();
		neuerMitarbeiter.setNutzer(neuerNutzer);
		neuerMitarbeiter.setFirma(firma);
		firma.getMitarbeiter().add(neuerMitarbeiter);

		nutzerService.registrieren(neuerNutzer, firma);
	}

	public void bestehendenNutzerEinladen(Nutzer bestehenderNutzer, Firma firma)
			throws Exception {
		istNutzerBerechtigt(firma, Firmenrolle.Administrator);

		Mitarbeiter mitarbeiter = new Mitarbeiter(firma, bestehenderNutzer);
		bestehenderNutzer.getFirmen().add(firma);

		standardNachrichten.zuFirmaHinzugefuegt(bestehenderNutzer, firma, ausfuehrenderNutzer);
	}

	private void istNutzerBerechtigt(Firma firma, Firmenrolle benoetigteRolle)
			throws Exception {
		for (Mitarbeiter mitarbeiter : firma.getMitarbeiter()) {
			if (mitarbeiter.equals(getNutzer())) {
				if (mitarbeiter.isAdmin()) {
					return;
				} else
					throw new FirmenSecurityException(getNutzer(), Firmenrolle.Administrator, "nutzer einladen");
			}
		}
		throw new Exception("Nutzer ist kein Mitarbeiter der Firma.");
	}

	@Override
	public void NutzerEntfernen(Firma firma, Mitarbeiter zuEntfernenderNutzer)
			throws Exception {

		istNutzerBerechtigt(firma, Firmenrolle.Administrator);

		zuEntfernenderNutzer.getNutzer().getFirmen().remove(firma);
		firma.getMitarbeiter().remove(zuEntfernenderNutzer);
		// TODO Marcel übernehme Änderung in DB

		standardNachrichten.vonFirmaEntfernt(zuEntfernenderNutzer.getNutzer(), firma, getNutzer());
	}

	@Override
	public void SetzeNutzerRolle(Firma firma, Mitarbeiter nutzer, Firmenrolle rolle)
			throws Exception {

		istNutzerBerechtigt(firma, Firmenrolle.Administrator);
 
		nutzer.setFirmenrolle(rolle);
	} 

	@Override
	public void loescheFirma(Firma firma, Nutzer angemeldeterNutzer) {
		standardNachrichten.firmaGeloescht(firma);
		firma.getMitarbeiter().forEach(m -> {
			m.getNutzer().getFirmen().remove(firma);
		});

		// TODO Änderung der Mitarbeiter abspeichern und Firma löschen!
	}

}
