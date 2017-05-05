package de.sitescrawler.applikation;
 
import java.io.Serializable; 

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import de.sitescrawler.jpa.*; 

@SessionScoped
@Named("firmen")
public class FirmenBean implements Serializable {
	 
		private static final long serialVersionUID = 1L;
		
		@Inject
		private DataBean dataBean;
		
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
		}
		
		public void mitarbeiterEinladen(){
			//TODO Nutzer einladen
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
			
			ausgewaehlteFirma.getMitarbeiter().add(dummyMitarbeiter); 
			
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
		
		public void add(Mitarbeiter mitarbeiter){
			
			aktuelleFiltergruppe.getEmpfaenger().add(mitarbeiter.getNutzer());
			
			RequestContext.getCurrentInstance().update("gruppen-auswahl");
		}
		
		public void remove(Mitarbeiter mitarbeiter){
			aktuelleFiltergruppe.getEmpfaenger().remove(mitarbeiter);
			RequestContext.getCurrentInstance().update("gruppen-auswahl");			
		}
}
