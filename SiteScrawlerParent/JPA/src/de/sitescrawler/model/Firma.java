package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.List;

public class Firma {
	private String name;
	private List<FirmenFilterGruppe> filtergruppen = new ArrayList<>();
	private List<FilterProfil> filterprofile = new ArrayList<>();
	private List<Mitarbeiter> mitarbeiter = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<FirmenFilterGruppe> getFiltergruppen() {
		return filtergruppen;
	}
	public void setFiltergruppen(List<FirmenFilterGruppe> filtergruppen) {
		this.filtergruppen = filtergruppen;
	}
	public List<FilterProfil> getFilterprofile() {
		return filterprofile;
	}
	public void setFilterprofile(List<FilterProfil> filterprofile) {
		this.filterprofile = filterprofile;
	}
	public List<Mitarbeiter> getMitarbeiter() {
		return mitarbeiter;
	}
	public void setMitarbeiter(List<Mitarbeiter> mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}
	
}
