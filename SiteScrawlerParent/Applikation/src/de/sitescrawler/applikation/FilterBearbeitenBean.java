package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

	private String ausgewaehlteTagesoption = TAEGLICH;

	private Filterprofil neuesFilterprofil = new Filterprofil();

	private Filterprofil zuAenderndesProfil = new Filterprofil();
	private Filterprofil zuAenderndesProfilOriginal;

	public void setParameter(Filterprofilgruppe filtergruppe, Set<Filterprofil> filterprofile) {
		this.filtergruppe = filtergruppe;
		this.filterprofile = filterprofile;
	}

	public String getAusgewaehlteTagesoption() {
		return ausgewaehlteTagesoption;
	}

	public void setAusgewaehlteTagesoption(String ausgewaehlteTagesoption) {
		this.ausgewaehlteTagesoption = ausgewaehlteTagesoption;
		filterManager.speichereAenderung(getFiltergruppe().getFiltermanager());

		speichereAenderungAnFiltergruppe(" gewähltes Intervall: " + ausgewaehlteTagesoption);
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

}
