package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.sitescrawler.model.Archiveintrag;
import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterGruppe;
import de.sitescrawler.model.FilterProfil;
import de.sitescrawler.utility.DateUtils;

@SessionScoped
@Named("data")
public class DataBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private FilterGruppe filtergruppe; 
	
	private List<FilterGruppe> filtergruppen = new ArrayList<>();
	
	public DataBean(){
		List<FilterProfil> fps1 = new ArrayList<>();
	    
		for (int i = 20; i < 30; i++) {
			FilterProfil fp1 = new FilterProfil();
			for (int j = 0; j < 30; j++) {
				fp1.getTags().add("Tag"+j);
			}
			fp1.setTitel("Filterprofil"+i);
			fps1.add(fp1);
		}
		
	    FilterGruppe fg = new FilterGruppe();
	    
	    fg.setFilterprofile(fps1);
	    fg.setTitel("Meine Filtergruppe");
	    fg.setArchiveintraege(getDummyArchiveintraege("Meine Filtergruppe"));
	    setFiltergruppe(fg);
	    
	    filtergruppen.add(fg);
	     
	    for (int i = 0; i < 3; i++) {
			FilterGruppe fig = new FilterGruppe();
			fig.setTitel("Filtergruppe von Firma" + i);
			fig.setArchiveintraege(getDummyArchiveintraege("Firma" + i));
			filtergruppen.add(fig);
			
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

	public FilterGruppe getFiltergruppe() {
		return filtergruppe;
	}

	public void setFiltergruppe(FilterGruppe filtergruppe) {
		this.filtergruppe = filtergruppe;
	}
	
	public List<FilterGruppe> getFiltergruppen() {
		return filtergruppen;
	}
	
	public void setFiltergruppen(List<FilterGruppe> filtergruppen) {
		this.filtergruppen = filtergruppen;
	} 
}
