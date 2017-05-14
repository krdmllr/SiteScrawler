package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;
import net.bootsfaces.utils.FacesMessages;

@SessionScoped
@Named("nutzerprofil")
public class NutzerProfilBean implements Serializable
{

    private static final long   serialVersionUID = 1L;
    private static final Logger LOGGER           = Logger.getLogger("de.sitescrawler.logger");

    private String              altesPasswort;
    private String              neuesPasswort;
    private String              neueEmail;

    @Inject
    private INutzerService      nutzerService;

    @Inject
    private INutzerDatenService nutzerDatenService;

    @Inject
    private loginBean           loginBean;

    public Nutzer getNutzer()
    {
        return this.nutzerDatenService.getNutzer();
    }

    public void setNeuesPasswort()
    {
        if (this.nutzerDatenService.verifizierePasswort(this.altesPasswort))
        {
            this.nutzerDatenService.aenderePasswort(this.neuesPasswort, this.altesPasswort);
        }
        else
        {
            // TODO wird nicht angezeigt
            FacesMessages.error("Falsches Passwort.", "Ihr eingegebenes Passwort ist ungültig.");
        }
    }

    public void setNeueEmail()
    {
        if (this.nutzerDatenService.verifizierePasswort(this.altesPasswort))
        {
            this.nutzerDatenService.aendereEmailAdresse(this.neueEmail, this.altesPasswort);
        }
        else
        {
            // TODO wird nicht angezeigt
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Falsches Passwort.", "Ihr eingegebenes Passwort ist ungültig."));
            FacesMessages.error("Falsches Passwort.", "Ihr eingegebenes Passwort ist ungültig.");
        }
    }

    public void loescheNutzer()
    {
        if (this.nutzerDatenService.verifizierePasswort(this.altesPasswort))
        {
            this.nutzerDatenService.loescheNutzer(this.altesPasswort);
            NutzerProfilBean.LOGGER.info("Account von Nutzer " + this.getNutzer() + " wurde gelöscht");
            this.loginBean.logout();
        }
        else
        {
            // TODO wird nicht angezeigt
            FacesMessages.error("Falsches Passwort.", "Ihr eingegebenes Passwort ist ungültig.");
        }
    }

    public void speichereAenerungen()
    {
        this.nutzerService.nutzerMergen(this.getNutzer());
    }

    public String getAltesPasswort()
    {
        return this.altesPasswort;
    }

    public void setAltesPasswort(String altesPasswort)
    {
        this.altesPasswort = altesPasswort;
    }

    public String getNeuesPasswort()
    {
        return this.neuesPasswort;
    }

    public void setNeuesPasswort(String neuesPasswort)
    {
        this.neuesPasswort = neuesPasswort;
    }

    public String getNeueEmail()
    {
        return this.neueEmail;
    }

    public void setNeueEmail(String neueEmail)
    {
        this.neueEmail = neueEmail;
    }

}
