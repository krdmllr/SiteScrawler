package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.TreeNode;

import de.sitescrawler.model.Archiveintrag;
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
	
	private String ausgewaehlteTagesoption = TAEGLICH; 
	
	
	
	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
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
		return  dataBean.getFiltergruppe().getFilterprofile().contains(profil);
	}
	
	public void addProfil(FilterProfil profil){
		dataBean.getFiltergruppe().getFilterprofile().add(profil);
	}
	
	public void profilVonGruppeEntfernen(FilterProfil profil){
		dataBean.getFiltergruppe().getFilterprofile().remove(profil);
	}
	
	public void deleteProfil(FilterProfil profil){
		dataBean.getFilterprofile().remove(profil);
	}  

	public void addTageszeiten() {
		dataBean.getFiltergruppe().getTageszeiten().add(DateUtils.asDate(LocalDateTime.now()));
	}
	
	public void removeTageszeiten(Date date){
		dataBean.getFiltergruppe().getTageszeiten().remove(date);
	}
}
