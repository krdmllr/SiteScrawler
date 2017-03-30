package de.sitescrawler.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the rolle database table.
 * 
 */
@Entity
@NamedQuery(name="Rolle.findAll", query="SELECT r FROM Rolle r")
public class Rolle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String rolle;

	//bi-directional many-to-many association to Nutzer
//	@ManyToMany(mappedBy="rollen")
//	private List<Nutzer> nutzer;

	public Rolle() {
	}

	public String getRolle() {
		return this.rolle;
	}

	public void setRolle(String rolle) {
		this.rolle = rolle;
	}

//	public List<Nutzer> getNutzer() {
//		return this.nutzer;
//	}
//
//	public void setNutzer(List<Nutzer> nutzer) {
//		this.nutzer = nutzer;
//	}

}