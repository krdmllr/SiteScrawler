package de.sitescrawler.model;

import java.util.List;

public class FilterGruppe {
	private String Titel;
	private List<FilterProfil> Filterprofile;
	
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
}
