package de.sitescrawler.model;

public class Mitarbeiter {
	
	private Boolean isAdmin;
	private Benutzer nutzeraccount;
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Benutzer getNutzeraccount() {
		return nutzeraccount;
	}
	public void setNutzeraccount(Benutzer nutzeraccount) {
		this.nutzeraccount = nutzeraccount;
	}
}
