package de.sitescrawler.email;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.email.interfaces.IStandardNachrichtenService;
import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Nutzer;

@ApplicationScoped
public class StandardNachrichtenService implements IStandardNachrichtenService
{

    // Globalen Logger holen
    private final static Logger LOGGER                = Logger.getLogger("de.sitescrawler.logger");

    private final String        BITTE_PASSWORT_SETZEN = "Ihnen wurde ein temporäres Passwort zugeteilt. Bitte ändern Sie dieses nach Ihrer ersten Anmeldung.";
    private final String        PASSWORT_TEXT         = "Das temporäre Passwort lautet:  ";

    @Inject
    private IMailSenderService  mailSenderService;

    @Override
    public void registrierungsMail(Nutzer nutzer) throws ServiceUnavailableException
    {
        String betreff = "Ihr neues Konto bei sitescrawler.de";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Herzlich willkommen " + nutzer.getGanzenNamen() + "!\n");
        bodyBuilder.append("Ihr Konto wurde erfolgreich angelegt.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append(this.BITTE_PASSWORT_SETZEN + "\n");
        bodyBuilder.append(this.PASSWORT_TEXT + nutzer.getPasswort() + "\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        this.mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
    }

    @Override
    public void registrierungUeberFirma(Nutzer nutzer, Firma firma) throws ServiceUnavailableException
    {
        String betreff = "Ihr neues Konto bei sitescrawler.de";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Herzlich willkommen " + nutzer.getGanzenNamen() + "!\n");
        bodyBuilder.append("Das Unternehmen " + firma.getName() + " hat für Sie einen Account angelegt.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append(this.BITTE_PASSWORT_SETZEN + "\n");
        bodyBuilder.append(this.PASSWORT_TEXT + nutzer.getPasswort() + "\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        this.mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);

    }

    @Override
    public void neuesPasswortGesetzt(Nutzer nutzer, LocalDateTime ruecksetzZeitangabe)
    {
        String betreff = "Passwort auf sitescrawler.de geändert";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Hallo " + nutzer.getGanzenNamen() + ".\n");
        bodyBuilder.append("Wir senden Ihnen diese Nachricht, um Sie darüber zu informieren, dass Ihr Passwort auf sitescrawler.de geändert wurde.\n");
        // TODO Schreibe was man machen soll, wenn man das net selbst war.
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        try
        {
            this.mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
        }
        catch (ServiceUnavailableException e)
        {
            StandardNachrichtenService.LOGGER.log(Level.SEVERE, "Mail zu geändertem Passwort konnte nicht gesendet werden", e);
        }

    }

    @Override
    public void passwortZuruecksetzen(Nutzer nutzer, String temporaeresPasswort) throws ServiceUnavailableException
    {
        String betreff = "Ihr Passwort auf sitescrawler.de wurde zurückgesetzt.";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Hallo " + nutzer.getGanzenNamen() + ".\n");
        bodyBuilder.append("\n");
        bodyBuilder.append(this.BITTE_PASSWORT_SETZEN + "\n");
        bodyBuilder.append(this.PASSWORT_TEXT + nutzer.getPasswort() + "\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        this.mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);

    }

    @Override
    public void vonFirmaEntfernt(Nutzer nutzer, Firma firma, Nutzer ausfuehrenderNutzer)
    {
        String betreff = "SiteScrawler: Sie wurden aus " + firma.getName() + " entfernt.";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Hallo " + nutzer.getGanzenNamen() + ".\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("Sie wurden als Mitglied der Firma " + firma.getName() + " von " + ausfuehrenderNutzer.getGanzenNamen() + " entfernt.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        try
        {
            this.mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
        }
        catch (ServiceUnavailableException e)
        {
            StandardNachrichtenService.LOGGER.log(Level.SEVERE, "Sende von Information, dass Nutzer aus Firma entfernt wurde fehlgeschlagen", e);
        }

    }

    @Override
    public void zuFirmaHinzugefuegt(Nutzer nutzer, Firma firma, Nutzer ausfuehrenderNutzer)
    {
        String betreff = "SiteScrawler: Sie wurden zu Firma " + firma.getName() + " hinzugefügt.";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Hallo " + nutzer.getGanzenNamen() + ".\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("Sie wurden als Mitglied zur Firma " + firma.getName() + " hinzugefügt.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        try
        {
            this.mailSenderService.sendeMail(nutzer.getEmail(), betreff, body, false, null);
        }
        catch (ServiceUnavailableException e)
        {
            StandardNachrichtenService.LOGGER.log(Level.SEVERE, "Sende von Information, dass Nutzer firma zugeteilt wurde fehlgeschlagen", e);
        }

    }

    @Override
    public void firmaGeloescht(Firma firma)
    {
        List<Nutzer> nutzer = firma.getMitarbeiter().stream().map((mitarbeiter) -> mitarbeiter.getNutzer()).collect(Collectors.toList());

        String betreff = "SiteScrawler: Firma " + firma.getName() + " wurde entfernt.";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Sehr geehrter Nutzer,\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("Die Firma " + firma.getName() + " wurde von SiteScrawler.de entfernt.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        try
        {
            this.mailSenderService.sendeMail(nutzer, betreff, body, false, null);
        }
        catch (ServiceUnavailableException e)
        {
            StandardNachrichtenService.LOGGER.log(Level.SEVERE, "Senden von Information, dass Nutzer firma zugeteilt wurde fehlgeschlagen", e);
        }
    }

    @Override
    public void firmaAngenommen(Firma firma)
    {
        List<Nutzer> nutzer = firma.getMitarbeiter().stream().map((mitarbeiter) -> mitarbeiter.getNutzer()).collect(Collectors.toList());

        String betreff = "SiteScrawler: Firma " + firma.getName() + " wurde angenommen.";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Sehr geehrter Nutzer,\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("Die Firma " + firma.getName() + " wurde von SiteScrawler.de angenommen.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        try
        {
            this.mailSenderService.sendeMail(nutzer, betreff, body, false, null);
        }
        catch (ServiceUnavailableException e)
        {
            StandardNachrichtenService.LOGGER.log(Level.SEVERE, "Senden von Information, dass Firma angenommen wurde fehlgeschlagen", e);
        }
    }

    @Override
    public void firmaAbgelehnt(Firma firma)
    {
        List<Nutzer> nutzer = firma.getMitarbeiter().stream().map((mitarbeiter) -> mitarbeiter.getNutzer()).collect(Collectors.toList());

        String betreff = "SiteScrawler: Firma " + firma.getName() + " wurde abgelehnt.";
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("Sehr geehrter Nutzer,\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("Die Firma " + firma.getName() + " wurde von SiteScrawler.de abgelehnt.\n");
        bodyBuilder.append("\n");
        bodyBuilder.append("- SiteScrawler Team");
        String body = bodyBuilder.toString();

        try
        {
            this.mailSenderService.sendeMail(nutzer, betreff, body, false, null);
        }
        catch (ServiceUnavailableException e)
        {
            StandardNachrichtenService.LOGGER.log(Level.SEVERE, "Senden von Information, dass Firma abgelehnt wurde fehlgeschlagen", e);
        }
    }

}
