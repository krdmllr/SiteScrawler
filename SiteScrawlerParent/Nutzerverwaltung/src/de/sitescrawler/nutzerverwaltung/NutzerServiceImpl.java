package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

public class NutzerServiceImpl implements NutzerService, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;

	@Override
	public void registrieren(Nutzer nutzer) {
		this.nutzerSpeichern(nutzer);
	}

	@Override
	public void rolleAnlegen(Rolle rolle) {
		this.entityManager.merge(rolle);
	}

	@Override
	public Nutzer getNutzer(String uid) {
		TypedQuery<Nutzer> query = this.entityManager.createQuery("SELECT n FROM Nutzer n WHERE n.uid= :uid", Nutzer.class);
		query.setParameter("uid", uid);
		EntityGraph<?> entityGraph = this.entityManager.getEntityGraph("NutzerFull");
		query.setHint("javax.persistence.loadgraph", entityGraph);
		Nutzer nutzer = query.getSingleResult();
		return nutzer;
	}

	@Override
	public void nutzerSpeichern(Nutzer nutzer) {
		this.entityManager.merge(nutzer);
	}

}
