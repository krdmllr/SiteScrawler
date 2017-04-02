package de.sitescrawler.applikation;
 
import java.io.Serializable; 

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import de.sitescrawler.model.Benutzer;
import de.sitescrawler.model.FilterGruppe;
import de.sitescrawler.model.Firma;
import de.sitescrawler.model.FirmenFilterGruppe;
import de.sitescrawler.model.Mitarbeiter;

@SessionScoped
@Named("firmen")
public class FirmenBean implements Serializable {
	 
		private static final long serialVersionUID = 1L;
		
		@Inject
		private DataBean dataBean;
		
		private Firma ausgewaehlteFirma;
		
		private String neuerMitarbeiterEmail;
		
		private FirmenFilterGruppe aktuelleFiltergruppe;

		@PostConstruct
		void init() {
			setAusgewaehlteFirma(dataBean.getFirmen().get(0));
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
		}
		
		public void mitarbeiterEinladen(){
			//TODO Nutzer einladen
			System.out.println("lade ein: " + neuerMitarbeiterEmail);
			if(neuerMitarbeiterEmail == null || neuerMitarbeiterEmail.isEmpty() || !neuerMitarbeiterEmail.contains("@")) {
				System.out.println(neuerMitarbeiterEmail + " nicht gültig.");
				return;
			} 
			
			Benutzer dummyNutzer = new Benutzer();
			dummyNutzer.setVorname(neuerMitarbeiterEmail.split("@")[0]);
			dummyNutzer.setNachname(neuerMitarbeiterEmail.split("@")[1]);
			Mitarbeiter dummyMitarbeiter = new Mitarbeiter();
			dummyMitarbeiter.setNutzeraccount(dummyNutzer);
			
			ausgewaehlteFirma.getMitarbeiter().add(0, dummyMitarbeiter); 
			
			neuerMitarbeiterEmail = "";
		}

		public FirmenFilterGruppe getAktuelleFiltergruppe() {
			return aktuelleFiltergruppe;
		}

		public void setAktuelleFiltergruppe(FirmenFilterGruppe aktuelleFiltergruppe) {
			this.aktuelleFiltergruppe = aktuelleFiltergruppe;
		} 
		
		public Boolean isEmpfaengerAktuellerFilterGruppe(Mitarbeiter mitarbeiter){
			if(aktuelleFiltergruppe == null) return false;
			
			return aktuelleFiltergruppe.getEmpfaenger().contains(mitarbeiter);
		}
		
		public void add(Mitarbeiter mitarbeiter){
			
			aktuelleFiltergruppe.getEmpfaenger().add(mitarbeiter);
			
			RequestContext.getCurrentInstance().update("gruppen-auswahl");
		}
		
		public void remove(Mitarbeiter mitarbeiter){
			aktuelleFiltergruppe.getEmpfaenger().remove(mitarbeiter);
			RequestContext.getCurrentInstance().update("gruppen-auswahl");			
		}
}
