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
	
	private Filterprofil zuAenderndesProfil = new Filterprofil();
	private Filterprofil zuAenderndesProfilOriginal;
	
	public void setParameter(Filterprofilgruppe filtergruppe, Set<Filterprofil> filterprofile){
		this.filtergruppe = filtergruppe;
		this.filterprofile = filterprofile;
	}
	
	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
		
		//TODO:DB Ändere neue Option in Datenbank
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

	public Filterprofil getNeuesFilterprofil() {
		return neuesFilterprofil;
	}

	public void setNeuesFilterprofil(Filterprofil neuesFilterprofil) {
		this.neuesFilterprofil = neuesFilterprofil;
	}

	public Filterprofil getZuAenderndesProfil() {
		return zuAenderndesProfil;
	}

	public void setZuAenderndesProfil(Filterprofil zuAenderndesProfil) {
		this.zuAenderndesProfil = zuAenderndesProfil;
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
	
	public boolean isTaeglich(){
		return getAusgewaehlteTagesoption() == TAEGLICH;
	}
	
	public boolean isWoechentlich(){
		return getAusgewaehlteTagesoption() == WOECHENTLICH;
	}
	
	public boolean isZweiWoechentlich(){
		return getAusgewaehlteTagesoption() == ZWEI_WOECHENTLICH;
	}
	
	public boolean isMonatlich(){
		return getAusgewaehlteTagesoption() == MONATLICH;
	}
	
	public Boolean isInFiltergruppe(Filterprofil profil){ 
		return  getFiltergruppe().getFilterprofile().contains(profil);
	}

	/**
	 * Setzt und speichert, ob eine Email bei der Archiveintrags-Generierung an die Empfänger verschickt werden soll.
	 */
	public void setVerschickeEmail(boolean verschicke){
		//TODO:DB Änderung in Datenbank abspeichern	
	}
	
	/*
	 * Fügt der aktiven Filtergruppe ein neues Filterprofil hinzu.
	 */
	public void addProfil(Filterprofil profil){
		getFiltergruppe().getFilterprofile().add(profil);
		//TODO:DB Änderung in Datenbank abspeichern		
	}
	
	/**
	 * Entfernt das Filterprofil von der aktiven Filtergruppe.
	 * @param profil
	 */
	public void profilVonGruppeEntfernen(Filterprofil profil){
		getFiltergruppe().getFilterprofile().remove(profil);
		//TODO:DB Änderung in Datenbank abspeichern		
	}
	
	/**
	 * Löscht das Filterprofil aus dem Filtermanager und löscht es aus der aktiven Filtergruppe, falls es teil diser war.
	 * @param profil
	 */
	public void deleteProfil(Filterprofil profil){
		
		if(filtergruppe.getFilterprofile().contains(profil)){
			profilVonGruppeEntfernen(profil);
		}
		
		getFilterprofile().remove(profil);
				
		//TODO:DB Änderung in Datenbank abspeichern		
	}  
	
	/**
	 * Kopiert das gewählte Filterprofil in ein temporäres profil, dass bearbeitet werden kann.
	 * @param profil
	 */
	public void aendereProfil(Filterprofil profil){
		zuAenderndesProfilOriginal = profil;
		Filterprofil zuAenderndesKopie = new Filterprofil();
		zuAenderndesKopie.setFilterprofilname(zuAenderndesProfilOriginal.getFilterprofilname());
		zuAenderndesKopie.setTagstring(zuAenderndesProfilOriginal.getTagstring());
		
		setZuAenderndesProfil(zuAenderndesKopie);
	}
	
	/**
	 * Kopiert änderungen aus dem temporär erstellen Profil in das original Profil.
	 */
	public void modalAenderungSpeichern(){ 
		zuAenderndesProfilOriginal.setFilterprofilname(zuAenderndesProfil.getFilterprofilname());
		zuAenderndesProfilOriginal.setTagstring(zuAenderndesProfil.getTagstring());
		//TODO:DB Speicher Profiländerung in DB
	}

	/**
	 * Fügt der Filtergruppe eine neue Uhrzeit zur Archiveintraggenerierung hinzu.
	 */
	public void addTageszeiten() {
		Uhrzeit zeit = new Uhrzeit();
		LocalDateTime dateTime = DateUtils.rundeZeitpunkt(LocalDateTime.now());
		
		zeit.setUhrzeit(DateUtils.asDate(dateTime));
		getFiltergruppe().getUhrzeiten().add(zeit);
		//TODO:DB Speicher neue Zeit in DB
	}
	
	/**
	 * Entfernt eine Archiveintragserstellungs Uhrzeit aus der Filtergruppe.
	 * @param date
	 */
	public void removeTageszeiten(Uhrzeit date){
		getFiltergruppe().getUhrzeiten().remove(date);
		//TODO:DB Speicher Zeit in DB
	}
	
	/**
	 * Speichert das neue Filterprofil in die Filtergruppe
	 */
	public void neuesFilterprofilSpeichern(){
		if(neuesFilterprofil == null || dataBean == null) return;
		
		if(neuesFilterprofil.getFilterprofilname() == null || neuesFilterprofil.getFilterprofilname().isEmpty())
		{
			neuesFilterprofil.setFilterprofilname("Filterprofil " + dataBean.getNutzer().getFilterprofile().size());
		}
		
		getFilterprofile().add( neuesFilterprofil);
		neuesFilterprofil = new Filterprofil();
	}

}
