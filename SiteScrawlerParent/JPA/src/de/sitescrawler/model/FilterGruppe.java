package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterGruppe {
	private String Titel;
	private List<FilterProfil> Filterprofile = new ArrayList<>();
	private List<Archiveintrag> archiveintraege = new ArrayList<>();
	private List<Date> tageszeiten = new ArrayList<>();
	private Firma firma;
	
	private List<Mitarbeiter> empfaenger = new ArrayList<>();

	public List<Mitarbeiter> getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(List<Mitarbeiter> empfaenger) {
		this.empfaenger = empfaenger;
	}

	public List<Date> getTageszeiten() {
		return tageszeiten;
	}
	
	public String getTitel() {
		return Titel;
	}
	public void setTitel(String titel) {
		Titel = titel;
	}
	public List<FilterProfil> getFilterprofile() {
		return Filterprofile;
	}
	public void setFilterprofile(List<FilterProfil> filterprofile) {
		Filterprofile = filterprofile;
	}
	public List<Archiveintrag> getArchiveintraege() {
		return archiveintraege;
	}
	public void setArchiveintraege(List<Archiveintrag> archiveintraege) {
		this.archiveintraege = archiveintraege;
	}

	public Firma getFirma() {
		return firma;
	}

	public void setFirma(Firma firma) {
		this.firma = firma;
	}
	
	public Boolean isFirma(){
		return firma != null;
	}
}
