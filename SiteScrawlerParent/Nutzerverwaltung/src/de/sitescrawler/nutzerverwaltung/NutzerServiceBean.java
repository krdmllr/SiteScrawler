package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.email.ServiceUnavailableException;
import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Intervall;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;
import de.sitescrawler.model.ZeitIntervall;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;
import de.sitescrawler.nutzerverwaltung.interfaces.IPasswortService;
import de.sitescrawler.solr.interfaces.ISolrService;

@ApplicationScoped
@Named
@Transactional
public class NutzerServiceBean implements INutzerService, Serializable
{
    private static final Logger LOGGER           = Logger.getLogger("de.sitescrawler.logger");
    private static final long   serialVersionUID = 1L;
    @PersistenceContext
    private EntityManager       entityManager;
    @Inject
    ISolrService                solrService;
    @Inject
    private IMailSenderService  mailSenderService;
    @Inject
    private IPasswortService    passwortService;

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void rolleAnlegen(Rolle rolle)
    {
        this.entityManager.merge(rolle);
        NutzerServiceBean.LOGGER.info(rolle + " als Rolle angelegt.");
    }

    @Override
    public Nutzer getNutzer(String email)
    {
        NutzerServiceBean.LOGGER.info("Suche Nutzer in der DB mit Email " + email);
        TypedQuery<Nutzer> query = this.entityManager.createQuery("SELECT n FROM Nutzer n WHERE n.email= :email", Nutzer.class);
        query.setParameter("email", email);
        EntityGraph<?> entityGraph = this.entityManager.getEntityGraph("Nutzer.*");
        query.setHint("javax.persistence.loadgraph", entityGraph);
        Nutzer nutzer = query.getSingleResult();
        this.completeNutzer(nutzer);
        return nutzer;
    }

    /**
     * L�dt alle Daten aus der DB um den nutzer zu vervollst�ndigen
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
                    this.solrService.komplettiereArtikel(artikel);
                }
            }
        }
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void nutzerSpeichern(Nutzer nutzer)
    {
        NutzerServiceBean.LOGGER.info("Nutzer " + nutzer + " wird persistiert.");
        this.entityManager.persist(nutzer);
    }

    @Override
    @Transactional(value = TxType.REQUIRED)
    public void nutzerMergen(Nutzer nutzer)
    {
        NutzerServiceBean.LOGGER.info("Nutzer " + nutzer + " wird persistiert.");
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
    public void registrieren(Nutzer nutzer) throws ServiceUnavailableException
    {
        this.initNewNutzer(nutzer);
        this.passwortService.setNeuesPasswort(nutzer);
        this.sendeMail(nutzer);
        this.nutzerSpeichern(nutzer);
    }

    @Override
    public void passwortZuruecksetzen(Nutzer nutzer) throws ServiceUnavailableException
    {
        this.passwortService.setNeuesPasswort(nutzer);
        this.sendeMail(nutzer);
        this.nutzerMergen(nutzer);
    }

    @Override
    @Deprecated
    public void neuesPasswortSetzen(String token, String neuesPasswort)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void nutzerLoeschen(Nutzer nutzer)
    {
        Nutzer find = this.entityManager.find(Nutzer.class, nutzer.getIdentifikation());
        if (find != null)
        {
            this.entityManager.remove(find);
            NutzerServiceBean.LOGGER.info(nutzer + " wurde gelöscht.");
        }
        else
        {
            NutzerServiceBean.LOGGER.info(nutzer + " konnte nicht in der DB gefunden werden.");
        }
    }

    /**
     * Sendet eine Mail an den Nutzer mit dem neuen Passwort
     *
     * @param nutzer
     * @throws ServiceUnavailableException
     */
    private void sendeMail(Nutzer nutzer) throws ServiceUnavailableException
    {
        String emailAdresse = nutzer.getEmail();
        String subjekt = "";
        String body = "";
        boolean htmlBody = true;
        ByteArrayDataSource anhang = null;
        this.mailSenderService.sendeMail(emailAdresse, subjekt, body, htmlBody, anhang);
    }

    /**
     * Gibt dem Nutzer eine default Rolle und Filterprofilgruppe
     */
    private void initNewNutzer(Nutzer nutzer)
    {
        nutzer.getRollen().add(new Rolle("Registered"));
        Filterprofilgruppe filterprofilgruppe = new Filterprofilgruppe(nutzer, new Intervall(ZeitIntervall.TAEGLICH), "Meine Gruppe");
        filterprofilgruppe.setNutzer(nutzer);
        nutzer.getFilterprofilgruppen().add(filterprofilgruppe);
    }

}
