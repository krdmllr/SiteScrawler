package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import de.sitescrawler.exceptions.FirmenSecurityException;
import de.sitescrawler.firmenverwaltung.interfaces.IFirmenService;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Intervall;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.management.interfaces.IFilterManagerManager;
import de.sitescrawler.jpa.management.interfaces.IFirmenManager;
import de.sitescrawler.model.Firmenrolle;
import de.sitescrawler.model.ZeitIntervall;

/**
 *
 * @author robin FirmenBean, alle Methoden zur Firmenverwaltung.
 */
@SessionScoped
@Named("firmen")
public class FirmenBean implements Serializable
{

    // Globalen Logger holen
    private final static Logger LOGGER           = Logger.getLogger("de.sitescrawler.logger");

    private static final long   serialVersionUID = 1L;

    @Inject
    private DataBean            dataBean;

    @Inject
    private IFirmenManager      firmenManager;

    @Inject
    private IFirmenService      firmenService;

    private Firma               ausgewaehlteFirma;

    private String              neuerMitarbeiterEmail;

    private Filterprofilgruppe  aktuelleFiltergruppe;

    private String              neuerNutzerEmail;

    private Nutzer              neuerNutzer      = new Nutzer();

    private Nutzer              bestehenderNutzer;

    private Firma               neueFirma        = new Firma();

    private String              neueFirmaKommentar;

    private String              neueFiltergruppe;

    public String getNeueFiltergruppe()
    {
        return this.neueFiltergruppe;
    }

    public void setNeueFiltergruppe(String neueFiltergruppe)
    {
        this.neueFiltergruppe = neueFiltergruppe;
    }

    @Inject
    private IFilterManagerManager filterManager;

    public Firma getNeueFirma()
    {
        return this.neueFirma;
    }

    public void setNeueFirma(Firma neueFirma)
    {
        this.neueFirma = neueFirma;
    }

    public String getNeueFirmaKommentar()
    {
        return this.neueFirmaKommentar;
    }

    public void setNeueFirmaKommentar(String neueFirmaKommentar)
    {
        this.neueFirmaKommentar = neueFirmaKommentar;
    }

    @PostConstruct
    void init()
    {
        if (this.dataBean.getNutzer().getFirmen().size() > 0)
        {
            this.setAusgewaehlteFirma((Firma) this.dataBean.getNutzer().getFirmen().toArray()[0]);
        }
    }

    public void neueFiltergruppeHinzufuegen()
    {
        Filterprofilgruppe neueGruppe = new Filterprofilgruppe();
        neueGruppe.setTitel(this.neueFiltergruppe);
        neueGruppe.setFiltermanager(this.getAusgewaehlteFirma());
        neueGruppe.setIntervall(new Intervall(ZeitIntervall.TAEGLICH));
        this.getAusgewaehlteFirma().getFilterprofilgruppen().add(neueGruppe);
        this.filterManager.speichereAenderung(this.getAusgewaehlteFirma());
        this.neueFiltergruppe = "";
    }

    public boolean isNutzerAdmin()
    {
        Mitarbeiter mitarbeiterProfilVonNutzer = null;
        for (Mitarbeiter m : this.getAusgewaehlteFirma().getMitarbeiter())
        {
            if (m.getNutzer() == this.dataBean.getNutzer())
            {
                mitarbeiterProfilVonNutzer = m;
            }
        }

        return mitarbeiterProfilVonNutzer != null && mitarbeiterProfilVonNutzer.isAdmin();
    }

    public void neueFirmaVerwerfen()
    {
        this.neueFirma = new Firma();
    }

    public void neueFirmaErstellen()
    {
        FirmenBean.LOGGER.info("Erstelle Firma: " + this.neueFirma.getName() + " | " + this.neueFirma.getFirmenmail() + " | " + this.neueFirmaKommentar);

        this.firmenService.firmaBeantragen(this.neueFirma, this.neueFirmaKommentar);

        this.neueFirma = new Firma();
    }

    public Firma getAusgewaehlteFirma()
    {
        return this.ausgewaehlteFirma;
    }

    public String getNeuerMitarbeiterEmail()
    {
        return this.neuerMitarbeiterEmail;
    }

    public void setNeuerMitarbeiterEmail(String neuerMitarbeiterEmail)
    {
        this.neuerMitarbeiterEmail = neuerMitarbeiterEmail;
    }

    public void setAusgewaehlteFirma(Firma ausgewaehlteFirma)
    {
        this.ausgewaehlteFirma = ausgewaehlteFirma;
    }

    public void mitarbeiterEntfernen(Mitarbeiter mitarbeiter)
    {
        this.ausgewaehlteFirma.getMitarbeiter().remove(mitarbeiter);

        this.speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " entfernt.");
    }

    public List<Firma> getOffeneFirmenantraege()
    {
        return this.firmenService.offeneFirmenAntraege();
    }

    public void antragAnnehmen(Firma firma)
    {
        FirmenBean.LOGGER.info("Firma " + firma.getName() + " angenommen.");
        this.firmenService.bearbeiteFirmenAntrag(true, firma);
    }

    public void antragAblehnen(Firma firma)
    {
        FirmenBean.LOGGER.info("Firma " + firma.getName() + " abgelehnt.");
        this.firmenService.bearbeiteFirmenAntrag(false, firma);
    }

    public void mitarbeiterRegistrieren()
    {
        // Üerprüfe ob alle Felder angegeben wurden

        // Lade nutzer ein
        try
        {
            this.firmenService.nutzerEinladen(this.getAusgewaehlteFirma(), this.neuerNutzer.getEmail(), this.neuerNutzer.getVorname(),
                            this.neuerNutzer.getNachname());
        }
        catch (FirmenSecurityException securityException)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht registriert werden.", securityException);
        }
        catch (Exception ex)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht registriert werden.", ex);
        }
    }

    public void mitarbeiterEinladen()
    {
        // Üerprüfe ob alle Felder angegeben wurden

        // Lade nutzer ein
        try
        {
            this.firmenService.bestehendenNutzerEinladen(this.neuerMitarbeiterEmail, this.getAusgewaehlteFirma());
        }
        catch (FirmenSecurityException securityException)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht eingeladen werden.", securityException);
        }
        catch (Exception ex)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht eingeladen werden.", ex);
        }
    }

    public Filterprofilgruppe getAktuelleFiltergruppe()
    {
        return this.aktuelleFiltergruppe;
    }

    public void setAktuelleFiltergruppe(Filterprofilgruppe aktuelleFiltergruppe)
    {
        this.aktuelleFiltergruppe = aktuelleFiltergruppe;
    }

    public Boolean isEmpfaengerAktuellerFilterGruppe(Mitarbeiter mitarbeiter)
    {
        if (this.aktuelleFiltergruppe == null)
        {
            return false;
        }

        return this.aktuelleFiltergruppe.getEmpfaenger().contains(mitarbeiter);
    }

    /**
     * Fügt der aktuellen Filtergruppe den Mitarbeiter als Empfänger zu.
     *
     * @param mitarbeiter
     */
    public void add(Mitarbeiter mitarbeiter)
    {

        this.aktuelleFiltergruppe.getEmpfaenger().add(mitarbeiter.getNutzer());

        RequestContext.getCurrentInstance().update("gruppen-auswahl");

        this.speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " als Empfänger von " + this.aktuelleFiltergruppe.getTitel() + " hinzugefügt.");
    }

    /**
     * Entfernt den mitarbeiter aus der aktuellen Filtergruppe als Empfänger.
     *
     * @param mitarbeiter
     */
    public void remove(Mitarbeiter mitarbeiter)
    {
        this.aktuelleFiltergruppe.getEmpfaenger().remove(mitarbeiter);

        RequestContext.getCurrentInstance().update("gruppen-auswahl");

        this.speichereAenderung(mitarbeiter.getNutzer().getGanzenNamen() + " als Empfänger von " + this.aktuelleFiltergruppe.getTitel() + " entfernt.");
    }

    public void mitarbeiterZuAdmin(Mitarbeiter mitarbeiter)
    {
        try
        {
            this.firmenService.setzeNutzerRolle(this.getAusgewaehlteFirma(), mitarbeiter, Firmenrolle.Administrator);
        }
        catch (FirmenSecurityException securityException)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht als Admin gesetzt werden", securityException);
        }
        catch (Exception e)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht als Admin gesetzt werden.", e);
        }
    }

    public void mitarbeiterZuMitarbeiter(Mitarbeiter mitarbeiter)
    {
        try
        {
            this.firmenService.setzeNutzerRolle(this.getAusgewaehlteFirma(), mitarbeiter, Firmenrolle.Mitarbeiter);
        }
        catch (FirmenSecurityException securityException)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht als Mitarbeiter gesetzt werden.", securityException);
        }
        catch (Exception e)
        {
            FirmenBean.LOGGER.log(Level.SEVERE, "Mitarbeiter konnte nicht als Mitarbeiter gesetzt werden.", e);
        }
    }

    private void speichereAenderung(String beschreibung)
    {
        this.setAusgewaehlteFirma(this.firmenManager.speichereAenderungen(this.getAusgewaehlteFirma()));
        FirmenBean.LOGGER.log(Level.INFO, "Änderung in " + this.getAusgewaehlteFirma().getName() + ": " + beschreibung);
    }
}
