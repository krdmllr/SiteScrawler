package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;

public class NutzerServiceImpl implements INutzerService, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager; 

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

	@Override
	public boolean isEmailVerfuegbar(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registrieren(Nutzer nutzer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passwortZuruecksetzen(String email, String nutzername) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void neuesPasswortSetzen(String token, String neuesPasswort) {
		// TODO Auto-generated method stub
		
	}

}
