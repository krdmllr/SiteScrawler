package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;

@ApplicationScoped
@Named
public class NutzerServiceBean implements INutzerService, Serializable
{

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager     entityManager;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void rolleAnlegen(Rolle rolle)
    {
        this.entityManager.merge(rolle);
    }

    @Override
    public Nutzer getNutzer(String uid)
    {
        TypedQuery<Nutzer> query = this.entityManager.createQuery("SELECT n FROM Nutzer n WHERE n.identifikation= :uid", Nutzer.class);
        query.setParameter("uid", uid);
        EntityGraph<?> entityGraph = this.entityManager.getEntityGraph("Nutzer.*");
        query.setHint("javax.persistence.loadgraph", entityGraph);
        Nutzer nutzer = query.getSingleResult();
        return nutzer;
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void nutzerSpeichern(Nutzer nutzer)
    {
        this.entityManager.merge(nutzer);
    }

    @Override
    public boolean isEmailVerfuegbar(String email)
    {
        TypedQuery<Nutzer> query = this.entityManager.createQuery("SELECT n FROM Nutzer n WHERE n.email = :email", Nutzer.class);
        query.setParameter("email", email);
        boolean result = query.getResultList().isEmpty();
        return result;
    }

    @Override
    public void registrieren(Nutzer nutzer)
    {
        // TODO Email Verifizierung
        this.nutzerSpeichern(nutzer);

    }

    @Override
    public void passwortZuruecksetzen(String email, String nutzername)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void neuesPasswortSetzen(String token, String neuesPasswort)
    {
        // TODO Auto-generated method stub

    }

}
