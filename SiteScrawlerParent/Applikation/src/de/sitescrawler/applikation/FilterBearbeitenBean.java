package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Uhrzeit;
import de.sitescrawler.jpa.management.interfaces.IFilterManagerManager;
import de.sitescrawler.utility.DateUtils;

/**
 * 
 * @author robin FilterBearbeitenBean alle Methoden, um Filter zu verwalten.
 */
@SessionScoped
@Named("filterbearbeiten")
public class FilterBearbeitenBean implements Serializable {
	private static final long serialVersionUID = 1L;

	// Globalen Logger holen
	private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

	@Inject
	private IFilterManagerManager filterManager;

	private final String TAEGLICH = "Täglich";
	private final String WOECHENTLICH = "Wöchentlich";
	private final String ZWEI_WOECHENTLICH = "Zwei-Wöchentlich";
	private final String MONATLICH = "Monatlich";

	private Set<Filterprofil> filterprofile;

	private Filterprofilgruppe filtergruppe;
	
	private List<String> gewaehlteZeiten = new ArrayList<>();

	public List<String> getGewaehlteZeiten() {
		return gewaehlteZeiten;
	}

	public void setGewaehlteZeiten(List<String> gewaehlteZeiten) {
		this.gewaehlteZeiten = gewaehlteZeiten;
	}

	private String ausgewaehlteTagesoption = TAEGLICH;

	private Filterprofil neuesFilterprofil = new Filterprofil();

	private Filterprofil zuAenderndesProfil = new Filterprofil();
	private Filterprofil zuAenderndesProfilOriginal;
	
	private String gewaehlteEinzelUhrzeit; 

	public void setParameter(Filterprofilgruppe filtergruppe, Set<Filterprofil> filterprofile) {
		this.filtergruppe = filtergruppe;
		this.filterprofile = filterprofile;
		
		gewaehlteZeiten.clear();
		for(Uhrzeit zeit: filtergruppe.getUhrzeiten()){
			gewaehlteZeiten.add(zeitZuString(zeit.getUhrzeit()));
		}
	} 

	public DayOfWeek[] getMoeglicheWochentage(){
		return new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
	}
	
	public String[] getMoeglicheUhrzeiten(){
		return new String[]{"00:00", "00:01", "00:02", "00:03", "00:04", "00:05", "00:06", "00:07", "00:08", "00:09", "00:10", "00:11", "00:12", "00:13", "00:14", "00:15", "00:16", "00:17", "00:18", "00:19",  "00:20", "00:21", "00:22", "00:23"};
	}
	
	private String zeitZuString(Date date)
	{
		return date.getHours()+"";
	}
	
	private Date stringZuDate(String string)
	{
		int stringAlsInt = Integer.parseInt(string);
		return new Date(0,0,0,stringAlsInt,0);
	}
	
	public void speichereZeiten(){ 
		 
		getFiltergruppe().getUhrzeiten().clear();
		speichereAenderungAnFiltergruppe("Leere Uhrzeiten");	
		
		String alleZeiten = "";
		for(String zeit: gewaehlteZeiten)
		{
			alleZeiten += zeit + "; "; 
				Set<Filterprofilgruppe> dummyGruppe = new HashSet<>();
				dummyGruppe.add(getFiltergruppe());
				getFiltergruppe().getUhrzeiten().add(new Uhrzeit(stringZuDate(zeit), dummyGruppe));
				
		}
		
		for(Uhrzeit test : getFiltergruppe().getUhrzeiten())
		{
			System.out.println(test.getUhrzeit());
		}
		
		System.out.println(alleZeiten);
		
		speichereAenderungAnFiltergruppe("Neue Zeiten gespeichert " + alleZeiten);
	}
	
	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
		filterManager.speichereAenderung(getFiltergruppe().getFiltermanager());

		speichereAenderungAnFiltergruppe(" gewähltes Intervall: " + ausgewaehlteTagesoption);
	}
	
	public void speichereMonatlichenTermin(){ 
		String stunde = gewaehlteEinzelUhrzeit.replaceAll("[0:]+","");
		Date stundeAlsZeit = stringZuDate(stunde); 
		speichereAenderungAnFiltergruppe("Monatlicher Termin: " + filtergruppe.getMonatlicherTermin());
	}
	
	public void speichereWoechentlichenTermin(){ 
		
		speichereAenderungAnFiltergruppe("Wöchentlicher Termin: " + filtergruppe.getWochentag());
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

	public List<String> getTagesOptionen() {
		List<String> optionen = new ArrayList<>();
		optionen.add(TAEGLICH);
		optionen.add(WOECHENTLICH);
		optionen.add(ZWEI_WOECHENTLICH);
		optionen.add(MONATLICH);

		return optionen.stream().filter(p -> p != ausgewaehlteTagesoption).collect(Collectors.toList());
	}

	public boolean isTaeglich() {
		return getAusgewaehlteTagesoption() == TAEGLICH;
	}

	public boolean isWoechentlich() {
		return getAusgewaehlteTagesoption() == WOECHENTLICH;
	}

	public boolean isZweiWoechentlich() {
		return getAusgewaehlteTagesoption() == ZWEI_WOECHENTLICH;
	}

	public boolean isMonatlich() {
		return getAusgewaehlteTagesoption() == MONATLICH;
	}

	public Boolean isInFiltergruppe(Filterprofil profil) {
		System.out.println(getFiltergruppe().getFilterprofile().contains(profil));
		return getFiltergruppe().getFilterprofile().contains(profil);
	}

	/**
	 * Setzt und speichert, ob eine Email bei der Archiveintrags-Generierung an
	 * die Empfänger verschickt werden soll.
	 */
	public void setVerschickeEmail(boolean verschicke) {
		// TODO:DB Änderung in Datenbank abspeichern

		speichereAenderungAnFiltergruppe("versende Email and Empfänger: " + (verschicke ? "Ja" : "Nein"));
	}

	/*
	 * Fügt der aktiven Filtergruppe ein neues Filterprofil hinzu.
	 */
	public void addProfil(Filterprofil profil) {
		
		getFiltergruppe().getFilterprofile().add(profil);
		speichereAenderungAnFiltergruppe("Filterprofil " + profil.getFilterprofilname() + " zu Gruppe hinzugefuegt.");
	}

	/**
	 * Entfernt das Filterprofil von der aktiven Filtergruppe.
	 * 
	 * @param profil
	 */
	public void profilVonGruppeEntfernen(Filterprofil profil) {
		getFiltergruppe().getFilterprofile().remove(profil);
		// TODO:DB Änderung in Datenbank abspeichern
		speichereAenderungAnFiltergruppe("Filterprofil " + profil.getFilterprofilname() + " von Gruppe entfernt.");
	}

	/**
	 * Löscht das Filterprofil aus dem Filtermanager und löscht es aus der
	 * aktiven Filtergruppe, falls es teil diser war.
	 * 
	 * @param profil
	 */
	public void deleteProfil(Filterprofil profil) {

		for (Filterprofilgruppe gruppe : filtergruppe.getFiltermanager().getFilterprofilgruppen()) {
			if (gruppe.getFilterprofile().contains(profil)) {
				gruppe.getFilterprofile().remove(profil);
			}
		}

		getFilterprofile().remove(profil);

		speichereAenderungAnFiltermanager(
				"Filterprofil " + profil.getFilterprofilname() + " gelöscht und aus allen Filtergruppen entfernt.");
	}

	/**
	 * Kopiert das gewählte Filterprofil in ein temporäres profil, dass
	 * bearbeitet werden kann.
	 * 
	 * @param profil
	 */
	public void aendereProfil(Filterprofil profil) {
		zuAenderndesProfilOriginal = profil;
		Filterprofil zuAenderndesKopie = new Filterprofil();
		zuAenderndesKopie.setFilterprofilname(zuAenderndesProfilOriginal.getFilterprofilname());
		zuAenderndesKopie.setTagstring(zuAenderndesProfilOriginal.getTagstring());

		setZuAenderndesProfil(zuAenderndesKopie);
	}

	/**
	 * Kopiert änderungen aus dem temporär erstellen Profil in das original
	 * Profil.
	 */
	public void modalAenderungSpeichern() {
		zuAenderndesProfilOriginal.setFilterprofilname(zuAenderndesProfil.getFilterprofilname());
		zuAenderndesProfilOriginal.setTagstring(zuAenderndesProfil.getTagstring());

		speichereAenderungAnFiltermanager(
				"Filterprofil " + zuAenderndesProfilOriginal.getFilterprofilname() + " verändert.");
	}

	/**
	 * Fügt der Filtergruppe eine neue Uhrzeit zur Archiveintraggenerierung
	 * hinzu.
	 */
	public void addTageszeiten() {
		Uhrzeit zeit = new Uhrzeit();
		LocalDateTime dateTime = DateUtils.rundeZeitpunkt(LocalDateTime.now());

		zeit.setUhrzeit(DateUtils.asDate(dateTime));
		getFiltergruppe().getUhrzeiten().add(zeit);

		speichereAenderungAnFiltergruppe("Uhrzeit " + dateTime + " hinzugefügt.");
	}

	/**
	 * Entfernt eine Archiveintragserstellungs Uhrzeit aus der Filtergruppe.
	 * 
	 * @param date
	 */
	public void removeTageszeiten(Uhrzeit date) {
		getFiltergruppe().getUhrzeiten().remove(date);

		speichereAenderungAnFiltergruppe("Uhrzeit " + date.getUhrzeit() + " entfernt.");
	}

	/**
	 * Speichert das neue Filterprofil in die Filtergruppe
	 */
	public void neuesFilterprofilSpeichern() {
		if (neuesFilterprofil == null || dataBean == null)
			return;

		if (neuesFilterprofil.getFilterprofilname() == null || neuesFilterprofil.getFilterprofilname().isEmpty()) {
			neuesFilterprofil.setFilterprofilname("Filterprofil " + dataBean.getNutzer().getFilterprofile().size());
		}

		getFilterprofile().add(neuesFilterprofil);

		speichereAenderungAnFiltermanager(
				"Neue Filterprofil " + neuesFilterprofil.getFilterprofilname() + " erstellt.");

		neuesFilterprofil = new Filterprofil();
	}

	private void speichereAenderungAnFiltergruppe(String beschreibung) {
		filterManager.speichereAenderung(getFiltergruppe().getFiltermanager());
		LOGGER.log(Level.INFO, "Änderung in " + getFiltergruppe().getTitel() + ": " + beschreibung);
	}

	private void speichereAenderungAnFiltermanager(String beschreibung) {
		filterManager.speichereAenderung(getFiltergruppe().getFiltermanager());
		LOGGER.log(Level.INFO, "Änderung an " + getFiltergruppe().getFiltermanager() + ": " + beschreibung);
	}

	public String getGewaehlteEinzelUhrzeit() {
		return gewaehlteEinzelUhrzeit;
	}

	public void setGewaehlteEinzelUhrzeit(String gewaehlteEinzelUhrzeit) {
		this.gewaehlteEinzelUhrzeit = gewaehlteEinzelUhrzeit;
	}  
}
