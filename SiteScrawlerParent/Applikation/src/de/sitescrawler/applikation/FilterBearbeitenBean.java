package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
 
import de.sitescrawler.model.FilterGruppe;
import de.sitescrawler.model.FilterProfil;
import de.sitescrawler.utility.DateUtils;

@SessionScoped
@Named("filterbearbeiten")
public class FilterBearbeitenBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String TAEGLICH = "Täglich";
	private final String WOECHENTLICH = "Wöchentlich";
	private final String ZWEI_WOECHENTLICH = "Zwei-Wöchentlich";
	private final String MONATLICH = "Monatlich"; 
	
	private List<FilterProfil> filterprofile; 

	private FilterGruppe filtergruppe; 
	
	private String ausgewaehlteTagesoption = TAEGLICH; 
	
	private FilterProfil neuesFilterprofil = new FilterProfil();
	private String neuerTag;
	
	
	public void setParameter(FilterGruppe filtergruppe, List<FilterProfil> filterprofile){
		this.filtergruppe = filtergruppe;
		this.filterprofile = filterprofile;
	}
	
	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
	}
	
	public List<FilterProfil> getFilterprofile() {
		return filterprofile;
	}

	public void setFilterprofile(List<FilterProfil> filterprofile) {
		this.filterprofile = filterprofile;
	}

	public FilterGruppe getFiltergruppe() {
		return filtergruppe;
	}

	public void setFiltergruppe(FilterGruppe filtergruppe) {
		this.filtergruppe = filtergruppe;
	}

	@Inject
	private DataBean dataBean; 
	
	public List<String> getTagesOptionen(){
		List<String> optionen = new ArrayList<>();
		optionen.add(TAEGLICH);
		optionen.add(WOECHENTLICH);
		optionen.add(ZWEI_WOECHENTLICH);
		optionen.add(MONATLICH);
		
		return optionen.stream()
			    .filter(p -> p != ausgewaehlteTagesoption).collect(Collectors.toList());
	}
	
	public Boolean isInFiltergruppe(FilterProfil profil){ 
		return  getFiltergruppe().getFilterprofile().contains(profil);
	}
	
	public void addProfil(FilterProfil profil){
		getFiltergruppe().getFilterprofile().add(0, profil);
	}
	
	public void profilVonGruppeEntfernen(FilterProfil profil){
		getFiltergruppe().getFilterprofile().remove(profil);
	}
	
	public void deleteProfil(FilterProfil profil){
		getFilterprofile().remove(profil);
	}  

	public void addTageszeiten() {
		getFiltergruppe().getTageszeiten().add(DateUtils.asDate(LocalDateTime.now()));
	}
	
	public void removeTageszeiten(Date date){
		getFiltergruppe().getTageszeiten().remove(date);
	}
	
	public void neuesFilterprofilSpeichern(){
		if(neuesFilterprofil == null || dataBean == null) return;
		
		if(neuesFilterprofil.getTitel() == null || neuesFilterprofil.getTitel().isEmpty())
		{
			neuesFilterprofil.setTitel("Filterprofil " + dataBean.getFilterprofile().size());
		}
		
		getFilterprofile().add(0, neuesFilterprofil);
		neuesFilterprofil = new FilterProfil();
	}

	public FilterProfil getNeuesFilterprofil() {
		return neuesFilterprofil;
	}

	public void setNeuesFilterprofil(FilterProfil neuesFilterprofil) {
		this.neuesFilterprofil = neuesFilterprofil;
	}
	
	public void saveTag(){
		if(neuerTag == null || neuerTag.isEmpty() || neuesFilterprofil.getTags().contains(neuerTag)) return;
		
		neuesFilterprofil.addTag(neuerTag);
		neuerTag ="";
	}

	public String getNeuerTag() {
		return neuerTag;
	}

	public void setNeuerTag(String neuerTag) {
		this.neuerTag = neuerTag;
	}
}
