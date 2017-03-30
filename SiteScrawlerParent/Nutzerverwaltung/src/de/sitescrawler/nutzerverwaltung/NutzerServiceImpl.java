package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

public class NutzerServiceImpl implements NutzerService, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;

	@Override
	public void registrieren(Nutzer nutzer) {
		this.entityManager.merge(nutzer);
	}

	@Override
	public void rolleAnlegen(Rolle rolle) {
		this.entityManager.merge(rolle);
	}

	@Override
	public Nutzer getNutzer(String uid) {
		
		return null;
	}

}
