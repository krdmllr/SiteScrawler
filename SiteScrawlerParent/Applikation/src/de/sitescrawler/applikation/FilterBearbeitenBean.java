package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.TreeNode;

import de.sitescrawler.model.Archiveintrag;
import de.sitescrawler.model.FilterGruppe;
import de.sitescrawler.model.FilterProfil;

@SessionScoped
@Named("filterbearbeiten")
public class FilterBearbeitenBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private FilterGruppe filtergruppe;
	
	private List<FilterProfil> filterprofile;
	
	private String sendeRythmus;
	
	private Map<String, String> sendeRythmusOptionen = new HashMap<String, String>();

	public FilterBearbeitenBean(){
		
		List<FilterProfil> fps = new ArrayList<>();
	    for (int i = 0; i < 10; i++) {
			FilterProfil fp = new FilterProfil();
			fp.setTitel("Filterprofil"+i);
			fps.add(fp);
		}
	    
	    setFilterprofile(fps);
	    
	    List<FilterProfil> fps1 = new ArrayList<>();
	    for (int i = 20; i < 30; i++) {
			FilterProfil fp1 = new FilterProfil();
			fp1.setTitel("Filterprofil"+i);
			fps1.add(fp1);
		}
	    FilterGruppe fg = new FilterGruppe();
	    fg.setFilterprofile(fps1);
	    fg.setTitel("Meine Filtergruppe");
	    setFiltergruppe(fg);
	    
	    sendeRythmusOptionen.put("Täglich","Täglich");
	    sendeRythmusOptionen.put("Wöchentlich","Wöchentlich");
	    sendeRythmusOptionen.put("Zwei-Wöchentlich", "Zwei-Wöchentlich");
	    sendeRythmusOptionen.put("Monatlich", "Monatlich");
	} 
	
	public void addProfil(FilterProfil profil){
		getFiltergruppe().getFilterprofile().add(profil);
	}
	
	public void deleteProfil(FilterProfil profil){
		getFilterprofile().remove(profil);
	}
	
	public FilterGruppe getFiltergruppe() {
		return filtergruppe;
	}

	private void setFiltergruppe(FilterGruppe filtergruppe) {
		this.filtergruppe = filtergruppe;
	}

	public List<FilterProfil> getFilterprofile() {
		return filterprofile;
	}

	private void setFilterprofile(List<FilterProfil> filterprofile) {
		this.filterprofile = filterprofile;
	}

	public String getSendeRythmus() {
		return sendeRythmus;
	}

	public void setSendeRythmus(String sendeRythmus) {
		this.sendeRythmus = sendeRythmus;
	}

	public Map<String, String> getSendeRythmusOptionen() {
		return sendeRythmusOptionen;
	}

	public void setSendeRythmusOptionen(Map<String, String> sendeRythmusOptionen) {
		this.sendeRythmusOptionen = sendeRythmusOptionen;
	} 
}
