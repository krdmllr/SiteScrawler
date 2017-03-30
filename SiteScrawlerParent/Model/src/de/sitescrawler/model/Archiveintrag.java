package de.sitescrawler.model;

import java.time.LocalDateTime;
import java.util.List;

public class Archiveintrag {
	private LocalDateTime erstellungsDatum;
	private List<Artikel> artikel;
	
	public Archiveintrag() {
	}
	
	public LocalDateTime getErstellungsDatum() {
		return erstellungsDatum;
	}
	public void setErstellungsDatum(LocalDateTime erstellungsDatum) {
		this.erstellungsDatum = erstellungsDatum;
	}
	public List<Artikel> getArtikel() {
		return artikel;
	}
	public void setArtikel(List<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	

}
