package de.sitescrawler.applikation;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;

@SessionScoped
@Named("profil")
public class ProfilBean implements Serializable
{

    private static final long   serialVersionUID = 1L;

    @Inject
    private DataBean            dataBean;

    @Inject
    private INutzerDatenService nutzerDatenService;

    public Nutzer getNutzer()
    {
        return this.nutzerDatenService.getNutzer();
    }

    public void setNeuesPasswort(String neuesPasswort, String altesPasswort)
    {
        if (!this.isPasswortValide(altesPasswort))
        {
            return;
        }

        this.nutzerDatenService.aenderePasswort(neuesPasswort, altesPasswort);
    }

    public void setNeueEmail(String email, String aktuellesPasswort)
    {
        if (!this.isPasswortValide(aktuellesPasswort))
        {
            return;
        }

        this.nutzerDatenService.aendereEmailAdresse(email, aktuellesPasswort);
    }

    public void setEmpfangeHtmlEmails(boolean empfangeHtmlEmail)
    {

        this.nutzerDatenService.aendereEmpfangeHtmlEmails(empfangeHtmlEmail);
    }

    public void loescheNutzer(String aktuellesPasswort)
    {

        if (!this.isPasswortValide(aktuellesPasswort))
        {
            return;
        }

        this.nutzerDatenService.loescheNutzer(aktuellesPasswort);

    }

    private boolean isPasswortValide(String aktuellesPasswort)
    {
        return this.nutzerDatenService.istPasswortValide(aktuellesPasswort);
    }
}
