package de.sitescrawler.model;

import java.util.Date;

public class Artikel {
	private Date erstellungsdatum;
	private String autor;
	private String titel;
	private String beschreibung;
	private String link;
	
	public Artikel() {
	}
	
	public Artikel(Date erstellungsdatum, String autor, String titel, String beschreibung, String link) {
		this.erstellungsdatum = erstellungsdatum;
		this.autor = autor;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.link = link;
	}



	public Date getErstellungsdatum() {
		return erstellungsdatum;
	}

	public void setErstellungsdatum(Date erstellungsdatum) {
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
