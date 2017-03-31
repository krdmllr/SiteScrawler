package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.List;

public class FilterGruppe {
	private String Titel;
	private List<FilterProfil> Filterprofile = new ArrayList<>();
	private List<Archiveintrag> archiveintraege = new ArrayList<>();
	
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
}
