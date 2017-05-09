package de.sitescrawler.applikation;
 
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import de.sitescrawler.jpa.*;
import de.sitescrawler.jpa.management.interfaces.IFirmenManager; 

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
		
		private Firma ausgewaehlteFirma;
		
		private String neuerMitarbeiterEmail;
		
		private Filterprofilgruppe aktuelleFiltergruppe;

		@PostConstruct
		void init() {
			if(dataBean.getNutzer().getFirmen().size() > 0)
			{
				setAusgewaehlteFirma((Firma)dataBean.getNutzer().getFirmen().toArray()[0]);
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
		
		public void mitarbeiterEntfernen(Mitarbeiter mitarbeiter){
			ausgewaehlteFirma.getMitarbeiter().remove(mitarbeiter);
			
			speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " entfernt.");
		}
		
		public void mitarbeiterEinladen(){
			//TODO Dummy einladung gegen richtige Einladung tauschen
			System.out.println("lade ein: " + neuerMitarbeiterEmail);
			if(neuerMitarbeiterEmail == null || neuerMitarbeiterEmail.isEmpty() || !neuerMitarbeiterEmail.contains("@")) {
				System.out.println(neuerMitarbeiterEmail + " nicht gültig.");
				return;
			} 
			
			Nutzer dummyNutzer = new Nutzer();
			dummyNutzer.setVorname(neuerMitarbeiterEmail.split("@")[0]);
			dummyNutzer.setNachname(neuerMitarbeiterEmail.split("@")[1]);
			Mitarbeiter dummyMitarbeiter = new Mitarbeiter();
			dummyMitarbeiter.setNutzer(dummyNutzer);
			
			//TODO E-Mail verschicken
			
			ausgewaehlteFirma.getMitarbeiter().add(dummyMitarbeiter); 
			
			speichereAenderung(dummyNutzer.getGanzenNamen() + " eingeladen.");
			
			neuerMitarbeiterEmail = "";
		}

		public Filterprofilgruppe getAktuelleFiltergruppe() {
			return aktuelleFiltergruppe;
		}

		public void setAktuelleFiltergruppe(Filterprofilgruppe aktuelleFiltergruppe) {
			this.aktuelleFiltergruppe = aktuelleFiltergruppe;
		} 
		
		public Boolean isEmpfaengerAktuellerFilterGruppe(Mitarbeiter mitarbeiter){
			if(aktuelleFiltergruppe == null) return false;
			
			return aktuelleFiltergruppe.getEmpfaenger().contains(mitarbeiter);
		}
		
		/**
		 * Fügt der aktuellen Filtergruppe den Mitarbeiter als Empfänger zu.
		 * @param mitarbeiter
		 */
		public void add(Mitarbeiter mitarbeiter){
			
			aktuelleFiltergruppe.getEmpfaenger().add(mitarbeiter.getNutzer());
			
			RequestContext.getCurrentInstance().update("gruppen-auswahl");
			
			speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " als Empfänger von " + aktuelleFiltergruppe.getTitel() + " hinzugefügt.");
		}
		
		/**
		 * Entfernt den mitarbeiter aus der aktuellen Filtergruppe als Empfänger.
		 * @param mitarbeiter
		 */
		public void remove(Mitarbeiter mitarbeiter){
			aktuelleFiltergruppe.getEmpfaenger().remove(mitarbeiter);
			
			RequestContext.getCurrentInstance().update("gruppen-auswahl");	
			
			speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " als Empfänger von " + aktuelleFiltergruppe.getTitel() + " entfernt.");
		}
		
		
		public void mitarbeiterZuAdmin(Mitarbeiter mitarbeiter){
			mitarbeiter.macheZuAdmin();
			
			speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " ist jetzt Administrator.");
		}
		
		public void mitarbeiterZuMitarbeiter(Mitarbeiter mitarbeiter){
			mitarbeiter.macheZuNutzer();
			
			speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " ist jetzt Mitarbeiter.");
		}
		
		private void speichereAenderung(String beschreibung){
			
			firmenManager.speichereAenderungen(getAusgewaehlteFirma());
			LOGGER.log(Level.INFO, "Änderung in " + getAusgewaehlteFirma().getName() + ": " + beschreibung);
		}
}
