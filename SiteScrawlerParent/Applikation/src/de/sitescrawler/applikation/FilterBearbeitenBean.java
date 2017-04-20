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
import de.sitescrawler.model.Filteprofil;
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
	
	private List<Filteprofil> filterprofile; 

	private FilterGruppe filtergruppe; 
	
	private String ausgewaehlteTagesoption = TAEGLICH; 
	
	private Filteprofil neuesFilterprofil = new Filteprofil();
	private String neuerTag;
	
	
	public void setParameter(FilterGruppe filtergruppe, List<Filteprofil> filterprofile){
		this.filtergruppe = filtergruppe;
		this.filterprofile = filterprofile;
	}
	
	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
	}
	
	public List<Filteprofil> getFilterprofile() {
		return filterprofile;
	}

	public void setFilterprofile(List<Filteprofil> filterprofile) {
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
	
	public Boolean isInFiltergruppe(Filteprofil profil){ 
		return  getFiltergruppe().getFilterprofile().contains(profil);
	}
	
	public void addProfil(Filteprofil profil){
		getFiltergruppe().getFilterprofile().add(0, profil);
	}
	
	public void profilVonGruppeEntfernen(Filteprofil profil){
		getFiltergruppe().getFilterprofile().remove(profil);
	}
	
	public void deleteProfil(Filteprofil profil){
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
		neuesFilterprofil = new Filteprofil();
	}

	public Filteprofil getNeuesFilterprofil() {
		return neuesFilterprofil;
	}

	public void setNeuesFilterprofil(Filteprofil neuesFilterprofil) {
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
