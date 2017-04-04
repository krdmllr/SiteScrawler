package de.sitescrawler.applikation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Firma;
import de.sitescrawler.model.FirmenFilterGruppe;
import de.sitescrawler.model.Mitarbeiter;

@SessionScoped
@Named("firmen")
public class FirmenBean implements Serializable
{

    private static final long  serialVersionUID = 1L;

    @Inject
    private DataBean           dataBean;

    private Firma              ausgewaehlteFirma;

    private String             neuerMitarbeiterEmail;

    private FirmenFilterGruppe aktuelleFiltergruppe;

    @PostConstruct
    void init()
    {
        this.setAusgewaehlteFirma(this.dataBean.getFirmen().get(0));
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
    }

    public void mitarbeiterEinladen()
    {
        // TODO Nutzer einladen
        System.out.println("lade ein: " + this.neuerMitarbeiterEmail);
        if (this.neuerMitarbeiterEmail == null || this.neuerMitarbeiterEmail.isEmpty() || !this.neuerMitarbeiterEmail.contains("@"))
        {
            System.out.println(this.neuerMitarbeiterEmail + " nicht gültig.");
            return;
        }

        Nutzer dummyNutzer = new Nutzer();
        dummyNutzer.setVorname(this.neuerMitarbeiterEmail.split("@")[0]);
        dummyNutzer.setNachname(this.neuerMitarbeiterEmail.split("@")[1]);
        Mitarbeiter dummyMitarbeiter = new Mitarbeiter();
        dummyMitarbeiter.setNutzeraccount(dummyNutzer);

        this.ausgewaehlteFirma.getMitarbeiter().add(0, dummyMitarbeiter);

        this.neuerMitarbeiterEmail = "";
    }

    public FirmenFilterGruppe getAktuelleFiltergruppe()
    {
        return this.aktuelleFiltergruppe;
    }

    public void setAktuelleFiltergruppe(FirmenFilterGruppe aktuelleFiltergruppe)
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

    public void add(Mitarbeiter mitarbeiter)
    {

        this.aktuelleFiltergruppe.getEmpfaenger().add(mitarbeiter);

        RequestContext.getCurrentInstance().update("gruppen-auswahl");
    }

    public void remove(Mitarbeiter mitarbeiter)
    {
        this.aktuelleFiltergruppe.getEmpfaenger().remove(mitarbeiter);
        RequestContext.getCurrentInstance().update("gruppen-auswahl");
    }
}
