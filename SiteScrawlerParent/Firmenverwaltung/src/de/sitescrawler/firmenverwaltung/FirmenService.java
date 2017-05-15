package de.sitescrawler.firmenverwaltung;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.email.interfaces.IStandardNachrichtenService;
import de.sitescrawler.exceptions.FirmenSecurityException;
import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.firmenverwaltung.interfaces.IFirmenService;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Firmenrolle;
import de.sitescrawler.model.Firmenstatus;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;

@SessionScoped
@Transactional
public class FirmenService implements Serializable, IFirmenService
{

    private static final long           serialVersionUID = 1L;

    // Globalen Logger holen
    private final static Logger         LOGGER           = Logger.getLogger("de.sitescrawler.logger");

    @PersistenceContext
    private EntityManager               entityManager;

    @Inject
    private INutzerService              nutzerService;

    @Inject
    private IMailSenderService          mailService;

    @Inject
    private IStandardNachrichtenService standardNachrichten;

    @Inject
    private INutzerDatenService         nutzerDatenService;

    private Nutzer                      ausfuehrenderNutzer;

    public Nutzer getNutzer()
    {
        if (this.ausfuehrenderNutzer == null)
        {
            this.ausfuehrenderNutzer = this.nutzerDatenService.getNutzer();
        }

        return this.ausfuehrenderNutzer;
    }

    @Override
    public boolean istFirmenMailVerfuegbar(String name)
    {
        TypedQuery<Nutzer> query = this.entityManager.createQuery("SELECT n FROM Nutzer n WHERE n.email = :email", Nutzer.class);
        query.setParameter("email", name);
        boolean result = query.getResultList().isEmpty();
        return result;
    }

    @Override
    public boolean firmaBeantragen(String firmenName, String firmenMail, String kommentar)
    {
        if (!this.istFirmenMailVerfuegbar(firmenMail))
        {
            FirmenService.LOGGER.info("Firmenmail " + firmenMail + " ist nicht verfügbar.");
            return false;
        }
        Firma firma = new Firma(firmenName);
        // Set firmen Email
        firma.setFirmenmail(firmenMail);
        // Set Status auf Beantragt
        firma.setStatus(Firmenstatus.BEANTRAGT);
        firma.setMaxfiltergruppe(20);
        // Speichere neue Firma in Datenbank
        firma = this.firmaSpeichern(firma);

        // Sende Information an alle Administratoren
        this.InformiereAdministratorenUeberNeueFirma(firma, kommentar);

        return true;
    }

    @Transactional(value = TxType.REQUIRED)
    private Firma firmaSpeichern(Firma firma)
    {
        return firma = this.entityManager.merge(firma);
    }

    private void InformiereAdministratorenUeberNeueFirma(Firma firma, String kommentar)
    {
        List<Nutzer> alleAdministratoren = this.nutzerService.getAlleAdministratoren();
        String betreff = "Neue Firma beantragt";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Nutzer " + this.getNutzer().getGanzenNamen() + " hat die Firma " + firma.getName() + " beantragt.");
        bodyBuilder.append("");

        if (kommentar != null && !kommentar.isEmpty())
        {
            bodyBuilder.append("Der Nutzer hat folgenden Kommentar angegeben:");
            bodyBuilder.append(kommentar);
        }

        try
        {
            this.mailService.sendeMail(alleAdministratoren, betreff, bodyBuilder.toString(), false, null);
        }
        catch (ServiceUnavailableException e)
        {
            FirmenService.LOGGER.log(Level.SEVERE, "Administratoren konnte nicht über neuen Firmenantrag informiert werden", e);
        }
    }

    @Override
    public void nutzerEinladen(Firma firma, String email, String vorname, String nachname) throws ServiceUnavailableException, Exception
    {

        this.istNutzerBerechtigt(firma, Firmenrolle.Administrator);

        Nutzer neuerNutzer = new Nutzer(email, vorname, nachname);
        neuerNutzer.getFirmen().add(firma);
        Mitarbeiter neuerMitarbeiter = new Mitarbeiter();
        neuerMitarbeiter.setFirmenrolle(Firmenrolle.Mitarbeiter);
        neuerMitarbeiter.setNutzer(neuerNutzer);
        neuerNutzer.getMitarbeiter().add(neuerMitarbeiter);
        neuerMitarbeiter.setFirma(firma);
        firma.getMitarbeiter().add(neuerMitarbeiter);

        this.nutzerService.registrieren(neuerNutzer, firma);
    }

    @Override
    public void bestehendenNutzerEinladen(Nutzer bestehenderNutzer, Firma firma) throws Exception
    {
        this.istNutzerBerechtigt(firma, Firmenrolle.Administrator);

        Mitarbeiter mitarbeiter = new Mitarbeiter(firma, bestehenderNutzer);
        bestehenderNutzer.getMitarbeiter().add(mitarbeiter);
        bestehenderNutzer.getFirmen().add(firma);

        this.standardNachrichten.zuFirmaHinzugefuegt(bestehenderNutzer, firma, this.ausfuehrenderNutzer);
    }

    private void istNutzerBerechtigt(Firma firma, Firmenrolle benoetigteRolle) throws Exception
    {
        for (Mitarbeiter mitarbeiter : firma.getMitarbeiter())
        {
            if (mitarbeiter.getNutzer().equals(this.getNutzer()))
            {
                if (mitarbeiter.isAdmin())
                {
                    return;
                }
                else
                {
                    throw new FirmenSecurityException(this.getNutzer(), Firmenrolle.Administrator, "nutzer einladen");
                }
            }
        }
        throw new Exception("Nutzer ist kein Mitarbeiter der Firma.");
    }

    @Override
    public void nutzerEntfernen(Firma firma, Mitarbeiter zuEntfernenderNutzer) throws Exception
    {

        this.istNutzerBerechtigt(firma, Firmenrolle.Administrator);

        zuEntfernenderNutzer.getNutzer().getFirmen().remove(firma);
        firma.getMitarbeiter().remove(zuEntfernenderNutzer);
        // übernehme Änderung in DB
        firma = this.firmaSpeichern(firma);

        this.standardNachrichten.vonFirmaEntfernt(zuEntfernenderNutzer.getNutzer(), firma, this.getNutzer());
    }

    @Override
    public void setzeNutzerRolle(Firma firma, Mitarbeiter nutzer, Firmenrolle rolle) throws Exception
    {

        this.istNutzerBerechtigt(firma, Firmenrolle.Administrator);

        nutzer.setFirmenrolle(rolle);
    }

    @Override
    public void loescheFirma(Firma firma, Nutzer angemeldeterNutzer)
    {
        this.standardNachrichten.firmaGeloescht(firma);
        firma.getMitarbeiter().forEach(m -> {
            Nutzer nutzer = m.getNutzer();
            nutzer.getFirmen().remove(firma);
            nutzer.getMitarbeiter().remove(m);
            // Änderung der Mitarbeiter abspeichern
            this.nutzerService.nutzerMergen(nutzer);
        });
        // Firma löschen
        this.firmaLoeschen(firma);

    }

    @Transactional(value = TxType.REQUIRED)
    private void firmaLoeschen(Firma firma)
    {
        Firma find = this.entityManager.find(Firma.class, firma.getIdentifikation());
        this.entityManager.remove(find);
        FirmenService.LOGGER.info("Firma " + firma + " wurde aus der DB gelöscht");
    }

    @Override
    public List<Firma> offeneFirmenAntraege()
    {
        TypedQuery<Firma> query = this.entityManager.createQuery("SELECT f FROM Firma f WHERE f.status = 'BEANTRAGT'", Firma.class);
        List<Firma> offeneAntraege = query.getResultList();
        return offeneAntraege;
    }

    @Override
    public void bearbeiteFirmenAntrag(boolean annehmen, Firma firma)
    {
        firma.setStatus(annehmen ? Firmenstatus.VERIFIZIERT : Firmenstatus.ABGELEHNT);
        firma = this.firmaSpeichern(firma);
        // TODO firma dem admin nutzer zuordnen
    }

}
