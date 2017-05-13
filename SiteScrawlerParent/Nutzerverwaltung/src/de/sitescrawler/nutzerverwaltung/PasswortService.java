package de.sitescrawler.nutzerverwaltung;

import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.nutzerverwaltung.interfaces.IPasswortService;

@ApplicationScoped
@Named
public class PasswortService implements IPasswortService
{
    private static final Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    private String genPasswort()
    {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.sitescrawler.nutzerverwaltung.IPasswortService#setNeuesPasswort(de.sitescrawler.jpa.Nutzer)
     */
    @Override
    public String setNeuesPasswort(Nutzer nutzer)
    {
        String passwort = this.genPasswort();
        nutzer.setPasswort(passwort);
        PasswortService.LOGGER.info("Neues Passwort" + passwort + " generiert für " + nutzer.getEmail() + " ");
        return passwort;

    }

}
