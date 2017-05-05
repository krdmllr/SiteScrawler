package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
  
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Uhrzeit;
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
	
	private Set<Filterprofil> filterprofile; 

	private Filterprofilgruppe filtergruppe; 
	
	private String ausgewaehlteTagesoption = TAEGLICH; 
	
	private Filterprofil neuesFilterprofil = new Filterprofil();
	private String neuerTag;
	
	
	public void setParameter(Filterprofilgruppe filtergruppe, Set<Filterprofil> filterprofile){
		this.filtergruppe = filtergruppe;
		this.filterprofile = filterprofile;
	}
	
	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
	}
	
	public Set<Filterprofil> getFilterprofile() {
		return filterprofile;
	}

	public void setFilterprofile(Set<Filterprofil> filterprofile) {
		this.filterprofile = filterprofile;
	}

	public Filterprofilgruppe getFiltergruppe() {
		return filtergruppe;
	}

	public void setFiltergruppe(Filterprofilgruppe filtergruppe) {
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
	
	public Boolean isInFiltergruppe(Filterprofil profil){ 
		return  getFiltergruppe().getFilterprofile().contains(profil);
	}
	
	public void addProfil(Filterprofil profil){
		getFiltergruppe().getFilterprofile().add(profil);
	}
	
	public void profilVonGruppeEntfernen(Filterprofil profil){
		getFiltergruppe().getFilterprofile().remove(profil);
	}
	
	public void deleteProfil(Filterprofil profil){
		getFilterprofile().remove(profil);
	}  

	public void addTageszeiten() {
		Uhrzeit zeit = new Uhrzeit();
		zeit.setUhrzeit(DateUtils.asDate(LocalDateTime.now()));
		getFiltergruppe().getUhrzeiten().add(zeit);
	}
	
	public void removeTageszeiten(Date date){
		getFiltergruppe().getUhrzeiten().remove(date);
	}
	
	public void neuesFilterprofilSpeichern(){
		if(neuesFilterprofil == null || dataBean == null) return;
		
		if(neuesFilterprofil.getFilterprofilname() == null || neuesFilterprofil.getFilterprofilname().isEmpty())
		{
			neuesFilterprofil.setFilterprofilname("Filterprofil " + dataBean.getNutzer().getFilterprofile().size());
		}
		
		getFilterprofile().add( neuesFilterprofil);
		neuesFilterprofil = new Filterprofil();
	}

	public Filterprofil getNeuesFilterprofil() {
		return neuesFilterprofil;
	}

	public void setNeuesFilterprofil(Filterprofil neuesFilterprofil) {
		this.neuesFilterprofil = neuesFilterprofil;
	}
	
	public void saveTag(){
		if(neuerTag == null || neuerTag.isEmpty() || neuesFilterprofil.getTagstring().contains(neuerTag)) return;
		
		neuesFilterprofil.setTagstring(neuesFilterprofil.getTagstring() + neuerTag);
		neuerTag ="";
	}

	public String getNeuerTag() {
		return neuerTag;
	}

	public void setNeuerTag(String neuerTag) {
		this.neuerTag = neuerTag;
	}
}
