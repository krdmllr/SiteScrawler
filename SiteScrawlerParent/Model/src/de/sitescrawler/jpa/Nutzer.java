package de.sitescrawler.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the nutzer database table.
 * 
 */
@Entity
@NamedQuery(name="Nutzer.findAll", query="SELECT n FROM Nutzer n")
@NamedEntityGraph(name="NutzerFull", includeAllAttributes = true)
public class Nutzer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	private String email;

	private String name;

	private String passwort;

	//bi-directional many-to-many association to Rolle
	@ManyToMany
	@JoinColumn(name="uid")
	private List<Rolle> rollen;

	public Nutzer() {
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public List<Rolle> getRollen() {
		return this.rollen;
	}

	public void setRollen(List<Rolle> rollen) {
		this.rollen = rollen;
	}

}