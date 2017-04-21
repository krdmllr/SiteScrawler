package de.sitescrawler.applikation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.resource.spi.ConfigProperty;

import de.sitescrawler.model.Archiveintrag;
import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.Benutzer;
import de.sitescrawler.model.FilterGruppe;
import de.sitescrawler.model.Filteprofil;
import de.sitescrawler.model.Firma;
import de.sitescrawler.model.FirmenFilterGruppe;
import de.sitescrawler.model.Mitarbeiter;
import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.utility.DateUtils;

@SessionScoped
@Named("data")
public class DataBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Benutzer nutzer;
	
	@Inject
	private ProjectConfig config;
	
	private FilterGruppe filtergruppe; 
	
	private FilterGruppe nutzerFiltergruppe;
	
	private List<Filteprofil> filterprofile = new ArrayList<>();
	
	private List<Firma> firmen = new ArrayList<>(); 

	public DataBean(){
	 
		nutzer = new Benutzer();
		nutzer.setNachname("Dieter");
		nutzer.setVorname("Hans");
	    
		for (int i = 0; i < 15; i++) {
			Filteprofil fp1 = new Filteprofil();
			for (int j = 0; j < 10; j++) {
				fp1.getTags().add("Tag"+j);
			}
			fp1.setTitel("Filterprofil"+i);
			filterprofile.add(fp1);
		}
		
		nutzerFiltergruppe = new FilterGruppe();
	     
		nutzerFiltergruppe.setTitel("Meine Filtergruppe");
		nutzerFiltergruppe.setArchiveintraege(getDummyArchiveintraege("Meine Filtergruppe"));
	    for (int i = 0; i < 10; i++) {
	    	if(i % 2 == 0)
	    		nutzerFiltergruppe.getFilterprofile().add(filterprofile.get(i));
		}
	    setFiltergruppe(nutzerFiltergruppe); 
	    
    	Firma firmaVonNutzer = new Firma();
    	firmaVonNutzer.setName("Eine Firma");
    	Mitarbeiter nutzerArbeiter1 = new Mitarbeiter();
    	nutzerArbeiter1.setIsAdmin(true);
    	nutzerArbeiter1.setNutzeraccount(nutzer);
    	firmaVonNutzer.getMitarbeiter().add(nutzerArbeiter1);
    	
    	Firma fremdFirma = new Firma();
    	fremdFirma.setName("Andere Firma");
    	Mitarbeiter nutzerArbeiter2 = new Mitarbeiter(); 
    	nutzerArbeiter2.setNutzeraccount(nutzer);
    	fremdFirma.getMitarbeiter().add(nutzerArbeiter2);
    	
    	//Testmitarbeiter 	
    	for (int i = 0; i < 20; i++) {
			Benutzer testNutzer = new Benutzer();
			testNutzer.setVorname("Vorname" +i);
			testNutzer.setNachname("nachname" +i);
			Mitarbeiter mitarbeiter = new Mitarbeiter();
			mitarbeiter.setNutzeraccount(testNutzer);
			
			firmaVonNutzer.getMitarbeiter().add(mitarbeiter);
			
			Benutzer testNutzer2 = new Benutzer();
			testNutzer2.setVorname("Vorname" +i);
			testNutzer2.setNachname("nachname" +i);
			Mitarbeiter mitarbeiter2 = new Mitarbeiter();
			mitarbeiter2.setNutzeraccount(testNutzer2);
			fremdFirma.getMitarbeiter().add(mitarbeiter2);
		}
    	fremdFirma.getMitarbeiter().get(3).setIsAdmin(true);
    	
    	//Testprofile
    	firmenDummyDaten(fremdFirma);
    	firmenDummyDaten(firmaVonNutzer);
    	
    	firmen.add(firmaVonNutzer);
    	firmen.add(fremdFirma); 
    	
	}

	@PostConstruct
	void init() { 
	}
	
	private void firmenDummyDaten(Firma firma){
		for (int i = 0; i < 10; i++) {
			Filteprofil testProfil = new Filteprofil();
			testProfil.setTitel("FirmenFilterprofil" + i);
			for (int j = 0; j < 5; j++) {
				testProfil.getTags().add("Tag"+j);
			}
			firma.getFilterprofile().add(testProfil);
		}
		
		for (int j = 0; j < 2; j++) {
			FirmenFilterGruppe testGruppe = new FirmenFilterGruppe();
			testGruppe.setTitel("FirmenFilterGruppe"+j);
			testGruppe.setFirma(firma);
			for (int k = 0; k < 10; k++) {
				testGruppe.getEmpfaenger().add(firma.getMitarbeiter().get(k));
			}
			
			for (int k = 0; k < 2; k++) {
				testGruppe.getFilterprofile().add(firma.getFilterprofile().get(k));
			} 	
			
			testGruppe.setArchiveintraege(getDummyArchiveintraege(testGruppe.getTitel() + "Eintrag"));
			
			firma.getFiltergruppen().add(testGruppe);
		}
	}
	
	private List<Archiveintrag> getDummyArchiveintraege(String name){
			List<Archiveintrag> list = new ArrayList();
		
		try{
			
			
			for (int i = 0; i < 20; i++) {
				Archiveintrag eintrag = new Archiveintrag();
				List<Artikel> artikelListe = new ArrayList<Artikel>(); 
				
				eintrag.setErstellungsDatum(DateUtils.asDate(LocalDateTime.now().minusDays(i))); 
				for (int j = 0; j < 30; j++) {
					Artikel artikel = new Artikel();
					artikel.setAutor(name +"Autor" + i + "|" + j);
					artikel.setBeschreibung(name +"Beschreibung" + i + "|" + j);
					artikel.setTitel(name +"Titel" + i + "|" + j);
					artikel.setErstellungsdatum(DateUtils.asDate(DateUtils.asLocalDateTime(eintrag.getErstellungsDatum()).plusMinutes(i)));
					artikelListe.add(artikel);
				}
				eintrag.setArtikel(artikelListe);
				list.add(eintrag);
			}
			
			
		}catch(Exception ex){
			 System.out.println(ex); 
		}
		finally{
			return list;
		}
	}
	
	public Benutzer getNutzer() {
		return nutzer;
	}

	public void setNutzer(Benutzer nutzer) {
		this.nutzer = nutzer;
	}

	public List<Firma> getFirmen() {
		return firmen;
	}

	public void setFirmen(List<Firma> firmen) {
		this.firmen = firmen;
	}
	
	public List<Filteprofil> getFilterprofile() {
		return filterprofile;
	}

	private void setFilterprofile(List<Filteprofil> filterprofile) {
		this.filterprofile = filterprofile;
	} 

	public FilterGruppe getFiltergruppe() {
		return filtergruppe;
	}

	public void setFiltergruppe(FilterGruppe filtergruppe) {
		this.filtergruppe = filtergruppe;
	} 

	public List<FilterGruppe> getFiltergruppen() {
		if(nutzer == null) return null;
		
		List<FilterGruppe> alleFiltergruppen = new ArrayList<>();
		alleFiltergruppen.add(nutzerFiltergruppe);
		
		for(Firma f : firmen)
		{
			for(FirmenFilterGruppe ffg : f.getFiltergruppen()){
				for(Mitarbeiter ma : ffg.getEmpfaenger()){
					if(ma.getNutzeraccount() == null)
					{
						System.out.println(ma);
					}
					if(ma.getNutzeraccount().equals(nutzer))
					{
						alleFiltergruppen.add(ffg);
					}
				} 
			}
		}
		
		return alleFiltergruppen;
	}

	public ProjectConfig getConfig() {
		return config;
	} 
}
