package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerService;
import de.sitescrawler.qualifier.Produktiv;
 
@SessionScoped
@Named
public class NutzerDatenService implements Serializable, INutzerDatenService
{

    private static final long serialVersionUID = 1L;
    private Nutzer            nutzer;
    @Inject
    private INutzerService    nutzerService;

    @Override
    public Nutzer getNutzer()
    {
        return this.nutzer;
    }

    @Override
    public void setNutzer(String uid)
    {
        this.nutzer = this.nutzerService.getNutzer(uid);
    }

    @Override
    public void aendereNutzernamen(String neuerNutzername, String passwort)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void aendereEmailAdresse(String neueEmailAdresse, String passwort)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void aenderePasswort(String neuesPasswort, String altesPasswort)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void loescheNutzer(String passwort)
    {
        // TODO Auto-generated method stub

    }

}
