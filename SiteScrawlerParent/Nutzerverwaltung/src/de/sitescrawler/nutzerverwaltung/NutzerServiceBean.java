package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;
import de.sitescrawler.solr.interfaces.ISolrService;

@ApplicationScoped
@Named
public class NutzerServiceBean implements INutzerService, Serializable
{

    private static final long serialVersionUID = 1L;
    @PersistenceContext
    private EntityManager     entityManager;
    @Inject
    ISolrService              solrService;

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
        this.completeNutzer(nutzer);
        return nutzer;
    }

    /**
     * Lädt alle Daten aus der DB um den nutzer zu vervollständigen
     *
     * @param nutzer
     */
    private void completeNutzer(Nutzer nutzer)
    {
        for (Filterprofilgruppe filterprofilgruppe : nutzer.getFilterprofilgruppen())
        {
            for (Archiveintrag archiveintrag : filterprofilgruppe.getArchiveintraege())
            {
                for (Artikel artikel : archiveintrag.getArtikel())
                {
                    String solrid = artikel.getSolrid();
                    // TODO auf neues Model anpassen mit Quellen
                    artikel = this.solrService.sucheArtikelAusID(solrid);
                }
            }
        }
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
