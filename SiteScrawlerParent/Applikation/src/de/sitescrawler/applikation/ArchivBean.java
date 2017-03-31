package de.sitescrawler.applikation;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import de.sitescrawler.model.Archiveintrag;
import de.sitescrawler.model.Artikel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList; 
import java.util.List; 

@SessionScoped
@Named("archiv")
public class ArchivBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Archiveintrag geweahlterArchiveintrag;
		
	public ArchivBean(){
		setGeweahlterArchiveintrag(getArchiveintraege().get(0));
	}
	
	public Archiveintrag getGeweahlterArchiveintrag() {
		return geweahlterArchiveintrag;
	}

	public void setGeweahlterArchiveintrag(Archiveintrag geweahlterArchiveintrag) {
		this.geweahlterArchiveintrag = geweahlterArchiveintrag;
	}

	public void buttonAction(Archiveintrag eintrag) {
		 setGeweahlterArchiveintrag(eintrag);
         addMessage("Archiveintrag vom " + eintrag.getErstellungsDatum() + " ausgewählt!");
    }   
	
	private void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public List<Archiveintrag> getArchiveintraege(){
		List<Archiveintrag> list = new ArrayList();
		
		try{
			
			
			for (int i = 0; i < 20; i++) {
				Archiveintrag eintrag = new Archiveintrag();
				List<Artikel> artikelListe = new ArrayList<Artikel>();

				eintrag.setErstellungsDatum(LocalDateTime.now().minusDays(i)); 
				for (int j = 0; j < 30; j++) {
					Artikel artikel = new Artikel();
					artikel.setAutor("Autor" + i + "|" + j);
					artikel.setBeschreibung("Beschreibung" + i + "|" + j);
					artikel.setTitel("Titel" + i + "|" + j);
					artikel.setErstellungsdatum(eintrag.getErstellungsDatum().plusMinutes(j));
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
}

