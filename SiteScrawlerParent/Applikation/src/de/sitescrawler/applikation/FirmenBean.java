package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.ServiceUnavailableException;
import javax.ws.rs.NotFoundException;

import org.primefaces.context.RequestContext;

import de.sitescrawler.exceptions.*;
import de.sitescrawler.firmenverwaltung.interfaces.IFirmenService;
import de.sitescrawler.jpa.*;
import de.sitescrawler.jpa.management.interfaces.IFirmenManager;
import de.sitescrawler.model.Firmenrolle;

/**
 * 
 * @author robin FirmenBean, alle Methoden zur Firmenverwaltung.
 */
@SessionScoped
@Named("firmen")
public class FirmenBean implements Serializable {

	// Globalen Logger holen
	private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

	private static final long serialVersionUID = 1L;

	@Inject
	private DataBean dataBean; 
	
	@Inject
	private IFirmenManager firmenManager;
	
	@Inject
	private IFirmenService firmenService;

	private Firma ausgewaehlteFirma;

	private String neuerMitarbeiterEmail;

	private Filterprofilgruppe aktuelleFiltergruppe;
	
	private String neuerNutzerEmail;
	
	private Nutzer neuerNutzer = new Nutzer();
	
	private Nutzer bestehenderNutzer;
	

	@PostConstruct
	void init() {
		if (dataBean.getNutzer().getFirmen().size() > 0) {
			setAusgewaehlteFirma((Firma) dataBean.getNutzer().getFirmen().toArray()[0]);
		}
	}

	public Firma getAusgewaehlteFirma() {
		return ausgewaehlteFirma;
	}

	public String getNeuerMitarbeiterEmail() {
		return neuerMitarbeiterEmail;
	}

	public void setNeuerMitarbeiterEmail(String neuerMitarbeiterEmail) {
		this.neuerMitarbeiterEmail = neuerMitarbeiterEmail;
	}

	public void setAusgewaehlteFirma(Firma ausgewaehlteFirma) {
		this.ausgewaehlteFirma = ausgewaehlteFirma;
	}

	public void mitarbeiterEntfernen(Mitarbeiter mitarbeiter) {
		ausgewaehlteFirma.getMitarbeiter().remove(mitarbeiter);

		speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " entfernt.");
	}
	
	public void mitarbeiterRegistrieren(){
		//Üerprüfe ob alle Felder angegeben wurden
		
		
		//Lade nutzer ein
		try{
			firmenService.nutzerEinladen(getAusgewaehlteFirma(), neuerNutzer.getEmail(), neuerNutzer.getVorname(), neuerNutzer.getNachname());
		}
		catch(FirmenSecurityException securityException)
		{
			
		} 
		catch(Exception ex)
		{
			
		}
	}

	public void mitarbeiterEinladen() {
		//Üerprüfe ob alle Felder angegeben wurden
		
		
		//Lade nutzer ein
		try{
			firmenService.bestehendenNutzerEinladen(neuerNutzer, getAusgewaehlteFirma());
		}
		catch(FirmenSecurityException securityException)
		{
			//TODO LOLOGGING
		} 
		catch(Exception ex)
		{
			
		}		
	}

	public Filterprofilgruppe getAktuelleFiltergruppe() {
		return aktuelleFiltergruppe;
	}

	public void setAktuelleFiltergruppe(Filterprofilgruppe aktuelleFiltergruppe) {
		this.aktuelleFiltergruppe = aktuelleFiltergruppe;
	}

	public Boolean isEmpfaengerAktuellerFilterGruppe(Mitarbeiter mitarbeiter) {
		if (aktuelleFiltergruppe == null)
			return false;

		return aktuelleFiltergruppe.getEmpfaenger().contains(mitarbeiter);
	}

	/**
	 * Fügt der aktuellen Filtergruppe den Mitarbeiter als Empfänger zu.
	 * 
	 * @param mitarbeiter
	 */
	public void add(Mitarbeiter mitarbeiter) {

		aktuelleFiltergruppe.getEmpfaenger().add(mitarbeiter.getNutzer());

		RequestContext.getCurrentInstance().update("gruppen-auswahl");

		speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " als Empfänger von "
				+ aktuelleFiltergruppe.getTitel() + " hinzugefügt.");
	}

	/**
	 * Entfernt den mitarbeiter aus der aktuellen Filtergruppe als Empfänger.
	 * 
	 * @param mitarbeiter
	 */
	public void remove(Mitarbeiter mitarbeiter) {
		aktuelleFiltergruppe.getEmpfaenger().remove(mitarbeiter);

		RequestContext.getCurrentInstance().update("gruppen-auswahl");

		speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " als Empfänger von "
				+ aktuelleFiltergruppe.getTitel() + " entfernt.");
	} 

	public void mitarbeiterZuAdmin(Mitarbeiter mitarbeiter) {
		try {
			firmenService.SetzeNutzerRolle(getAusgewaehlteFirma(), mitarbeiter, Firmenrolle.Administrator);
		} 
		catch(FirmenSecurityException securityException)
		{
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mitarbeiterZuMitarbeiter(Mitarbeiter mitarbeiter) {
		try {
			firmenService.SetzeNutzerRolle(getAusgewaehlteFirma(), mitarbeiter, Firmenrolle.Mitarbeiter);
		}
		catch(FirmenSecurityException securityException)
		{
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	private void speichereAenderung(String beschreibung) {

		firmenManager.speichereAenderungen(getAusgewaehlteFirma());
		LOGGER.log(Level.INFO, "Änderung in " + getAusgewaehlteFirma().getName() + ": " + beschreibung);
	}
}
