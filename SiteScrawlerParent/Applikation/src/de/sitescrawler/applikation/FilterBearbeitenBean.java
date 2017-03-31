package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
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
	
	@Inject
	private DataBean dataBean;
	 
	private List<FilterProfil> filterprofile;  

	public FilterBearbeitenBean(){
		
		List<FilterProfil> fps = new ArrayList<>();
	    for (int i = 0; i < 10; i++) {
			FilterProfil fp = new FilterProfil();
			fp.setTitel("Filterprofil"+i);
			fps.add(fp);
		}
	    
	    setFilterprofile(fps); 
	} 
	
	public Boolean isInFiltergruppe(FilterProfil profil){ 
		return  dataBean.getFiltergruppe().getFilterprofile().contains(profil);
	}
	
	public void addProfil(FilterProfil profil){
		dataBean.getFiltergruppe().getFilterprofile().add(profil);
	}
	
	public void deleteProfil(FilterProfil profil){
		getFilterprofile().remove(profil);
	} 

	public List<FilterProfil> getFilterprofile() {
		return filterprofile;
	}

	private void setFilterprofile(List<FilterProfil> filterprofile) {
		this.filterprofile = filterprofile;
	} 
}
