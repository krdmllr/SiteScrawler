package de.sitescrawler.model;

import java.time.LocalDateTime;

public class Artikel {
	private LocalDateTime erstellungsdatum;
	private String autor;
	private String titel;
	private String beschreibung;
	private String link;
	
	public Artikel() {
	}

	public LocalDateTime getErstellungsdatum() {
		return erstellungsdatum;
	}

	public void setErstellungsdatum(LocalDateTime erstellungsdatum) {
		this.erstellungsdatum = erstellungsdatum;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
}
