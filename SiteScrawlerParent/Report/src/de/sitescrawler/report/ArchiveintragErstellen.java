package de.sitescrawler.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.util.ByteArrayDataSource;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.formatierung.IFormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;
import de.sitescrawler.solr.interfaces.ISolrService;
import de.sitescrawler.utility.DateUtils;

@ApplicationScoped
public class ArchiveintragErstellen
{
    private final static Logger   LOGGER = Logger.getLogger("de.sitescrawler.logger");

    @Inject
    ISolrService                  solr;

    @Inject
    IMailSenderService            mailSenderService;

    @Inject
    IFiltergruppenZugriffsManager filtergruppenZugriff;

    @Inject
    IFormatiererService           formatiererService;

    @Inject
    IQuellenManager               quellenManager;

    public void erstelleReport(Filterprofilgruppe filtergruppe, LocalDateTime aktuelleZeit)
    { 
        List<Filterprofil> filterprofile = new ArrayList<>(filtergruppe.getFilterprofile());

        List<Artikel> artikel = this.solr.sucheArtikel(filterprofile, filtergruppe.getLetzteerstellung());
        
        filtergruppe.setLetzteerstellung(DateUtils.asDate(aktuelleZeit));

        // Ordnet den Artikeln ihre Quellen zu.
        Set<Artikel> artikelAlsSet = new HashSet<>();
        artikel.forEach(a -> {
            try
            {
                a.setQuelle(this.quellenManager.getQuelle(a.getQid()));
                artikelAlsSet.add(a);
            }
            catch (Exception ex)
            {
                ArchiveintragErstellen.LOGGER.log(Level.WARNING, "Für Artikel " + a.getTitel() + " aus Quelle mit ID: " + a.getQid()
                                                                 + " wurde keine Quelle in der Datenbank gefunden gefunden.");
            }
        });

        Archiveintrag archiveintrag = new Archiveintrag(filtergruppe, DateUtils.asDate(aktuelleZeit), artikelAlsSet);
        filtergruppe.getArchiveintraege().add(archiveintrag);

        this.filtergruppenZugriff.speicherArchiveintrag(archiveintrag);

        ByteArrayDataSource pdf = this.formatiererService.generierePdfZusammenfassung(archiveintrag);

        this.sendeMailAnEmfaenger(filtergruppe, aktuelleZeit, this.getNutzerHtmlEmpfang(filtergruppe), true, archiveintrag, pdf);
        this.sendeMailAnEmfaenger(filtergruppe, aktuelleZeit, this.getNutzerPlaintextEmpfang(filtergruppe), false, archiveintrag, pdf);
    }

    /**
     * Sendet eine Benachrichtigung �ber den generierten Archiveintrag an alle angegebenen Nutzer.
     *
     * @param empfaenger
     *            Empf�nger der Benachrichtigung
     * @param html
     *            Soll der Inhalt der Email als HTML formatiert werden.
     * @param archiveintrag
     *            Der Archiveintrag �ber den Informiert wird.
     * @param pdf
     *            Das PDF mit dem Inhalt des Archiveintrags.
     */
    private void sendeMailAnEmfaenger(Filterprofilgruppe filtergruppe, LocalDateTime aktuelleZeit, List<Nutzer> empfaenger, boolean html,
                                      Archiveintrag archiveintrag, ByteArrayDataSource pdf)
    {

        if (empfaenger.isEmpty())
        {
            return;
        }

        String body;
        String betreff = "SiteScrawler zusammenfassung von " + filtergruppe.getTitel() + " vom " + aktuelleZeit.format(DateUtils.getDateFormatter());

        // Generiere die Zusammenfassung je nach Einstellung
        if (html)
        {
            body = this.formatiererService.generiereHtmlZusammenfassung(archiveintrag);
        }
        else
        {
            body = this.formatiererService.generierePlaintextZusammenfassung(archiveintrag);
        }

        try
        {
            this.mailSenderService.sendeMail(empfaenger, betreff, body, html, pdf);
        }
        catch (ServiceUnavailableException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Gibt alle Empf�nger zur�ck, die ihre Mail mit HTML Elementen empfangen wollen.
     *
     * @return html Empf�nger.
     */
    private List<Nutzer> getNutzerHtmlEmpfang(Filterprofilgruppe filtergruppe)
    {
        List<Nutzer> alleNutzer = new ArrayList<>(filtergruppe.getEmpfaenger());

        // TODO HTML/Plain eigenschaft fehlt noch

        return alleNutzer;
    }

    /**
     * ` Gibt alle Empf�nger zur�ck, die ihre Mail im Plaintext empfangen wollen.
     *
     * @return Plaintext Empf�nger.
     */
    private List<Nutzer> getNutzerPlaintextEmpfang(Filterprofilgruppe filtergruppe)
    {
        List<Nutzer> alleNutzer = new ArrayList<>(filtergruppe.getEmpfaenger());

        // TODO HTML/Plain eigenschaft fehlt noch

        return alleNutzer;
    }
}
