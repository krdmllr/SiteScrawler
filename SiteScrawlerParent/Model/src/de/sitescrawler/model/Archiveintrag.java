package de.sitescrawler.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Archiveintrag {
	private Date erstellungsDatum;
	private List<Artikel> artikel;
	
	public Archiveintrag() {
	}
	
	public Date getErstellungsDatum() {
		return erstellungsDatum;
	}
	public void setErstellungsDatum(Date erstellungsDatum) {
		this.erstellungsDatum = erstellungsDatum;
	}
	public List<Artikel> getArtikel() {
		return artikel;
	}
	public void setArtikel(List<Artikel> artikel) {
		this.artikel = artikel;
	}
	
	

}
