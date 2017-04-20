package de.sitescrawler.model;

public class Benutzer {
	private String vorname; 
	private String nachname;
	
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	
	public String getGanzerName(){
		return vorname + " " + nachname;
	}
	
}
