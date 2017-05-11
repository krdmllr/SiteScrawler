package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;
import de.sitescrawler.qualifier.Produktiv;

/**
 * 
 * @author robin Methoden zur Verwaltung der Nutzer Daten.
 */
@Produktiv
@SessionScoped
@Named
public class NutzerDatenService implements Serializable, INutzerDatenService {

	private static final long serialVersionUID = 1L;
	private Nutzer nutzer;
	@Inject
	private INutzerService nutzerService;

	@Override
	public Nutzer getNutzer() {
		return this.nutzer;
	}

	@Override
	public void setNutzer(String email) {
		this.nutzer = this.nutzerService.getNutzer(email);
	}

	@Override
	public void aendereEmailAdresse(String neueEmailAdresse, String passwort) {
		if (this.nutzer.getPasswort().equals(passwort)) {
			this.nutzer.setEmail(neueEmailAdresse);
			this.nutzerService.nutzerSpeichern(this.nutzer);
		}

	}

	@Override
	public void aenderePasswort(String neuesPasswort, String altesPasswort) {
		if (this.nutzer.getPasswort().equals(altesPasswort)) {
			this.nutzer.setPasswort(neuesPasswort);
			this.nutzerService.nutzerSpeichern(this.nutzer);
		}

	}

	@Override
	public void loescheNutzer(String passwort) {
		if (this.nutzer.getPasswort().equals(passwort)) {
			this.nutzerService.nutzerLoeschen(this.nutzer);
		}
	}

}
