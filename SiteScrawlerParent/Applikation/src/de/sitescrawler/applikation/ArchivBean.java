package de.sitescrawler.applikation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.formatierung.IFormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenManager;
import de.sitescrawler.report.IReportService;

/**
 *
 * @author robin Archiv Bean
 */
@SessionScoped
@Named("archiv")
public class ArchivBean implements Serializable
{
    // Globalen Logger holen
    private final static Logger   LOGGER           = Logger.getLogger("de.sitescrawler.logger");

    private static final long     serialVersionUID = 1L;

    @Inject
    private DataBean              dataBean;

    @Inject
    private IReportService        reporterService;

    @Inject
    private IFiltergruppenManager filtergruppenManager;

    @Inject
    private IFormatiererService   formatiererService;

    private Archiveintrag         geweahlterArchiveintrag;
    private Artikel               geweahlterArtikel;

    private Date                  abZeitpunkt      = new Date();
    private Date                  bisZeitpunkt     = new Date();

    public ArchivBean()
    {

    }

    public Date getAbZeitpunkt()
    {
        return this.abZeitpunkt;
    }

    public void setAbZeitpunkt(Date abZeitpunkt)
    {
        this.abZeitpunkt = abZeitpunkt;
    }

    public Date getBisZeitpunkt()
    {
        return this.bisZeitpunkt;
    }

    public void setBisZeitpunkt(Date bisZeitpunkt)
    {
        this.bisZeitpunkt = bisZeitpunkt;
    }

    public Artikel getGeweahlterArtikel()
    {
        return this.geweahlterArtikel;
    }

    public void setGeweahlterArtikel(Artikel geweahlterArtikel)
    {
        this.geweahlterArtikel = geweahlterArtikel;
    }

    @PostConstruct
    void init()
    {
        this.setzeErstenArchiveintrag();
    }

    /**
     * Setzt den ersten Archiveintrag der aktuellen Filtergruppe als gewählten Archiveintrag.
     */
    private void setzeErstenArchiveintrag()
    {
        List<Archiveintrag> archiveintraege = this.getArchiveintraege();
        if (!archiveintraege.isEmpty())
        {
            this.setGeweahlterArchiveintrag(archiveintraege.get(0));
        }
    }

    /**
     * Löscht den gewählten Archiveintrag.
     */
    public void loescheArchiveintrag()
    {

        this.dataBean.getFiltergruppe().getArchiveintraege().remove(this.geweahlterArchiveintrag);
        this.filtergruppenManager.speichereAenderung(this.dataBean.getFiltergruppe());
        ArchivBean.LOGGER.log(Level.INFO, "Archiveintrag vom " + this.geweahlterArchiveintrag.getErstellungsdatum() + " wurde gelöscht.");

        this.setzeErstenArchiveintrag();
    }

    /**
     * Exportiert das PDF des gewählten Archiveintrages und bietet es dem Nutzer als Download an.
     */
    public void exportierePdf()
    {

        ByteArrayOutputStream pdfData = null;
        try
        {
            // Erstelle PDF
            pdfData = this.formatiererService.generierePdfZusammenfassungStream(this.getGeweahlterArchiveintrag());
        }
        catch (Exception ex)
        {
            ArchivBean.LOGGER.log(Level.SEVERE, "PDF Erstellung fehlgeschlagen.", ex);
        }

        if (pdfData != null)
        {
            try
            {
                // Lasse Nutzer PDF herunterladen
                this.downloadPdf(pdfData);
            }
            catch (IOException ex)
            {
                // TODO Auto-generated catch block
                ArchivBean.LOGGER.log(Level.SEVERE, "PDF Download fehlgeschlagen.", ex);
            }
        }
    }

    public void downloadPdf(ByteArrayOutputStream pdfData) throws IOException
    {

        String fileName = "Test Name Pressespiegel";

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer
                            // beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all
                                                      // types. Use if necessary ExternalContext#getMimeType() for
                                                      // auto-detection based on filename.
        ec.setResponseContentLength(pdfData.size()); // Set it with the file size. This header is optional. It will work
                                                     // if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup
                                                                                                  // magic is done here.
                                                                                                  // You can give it any
                                                                                                  // file name you want,
                                                                                                  // this only won't
                                                                                                  // work in MSIE, it
                                                                                                  // will use current
                                                                                                  // request URL as file
                                                                                                  // name instead.

        OutputStream output = ec.getResponseOutputStream();
        pdfData.writeTo(output);

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail
                               // since it's already written with a file and closed.
    }

    public void manuellCrawlen()
    {
        System.out.println("CRAWLE");
        this.reporterService.generiereManuellenReport(this.dataBean.getFiltergruppe());
        this.setzeErstenArchiveintrag();
    }

    public Archiveintrag getGeweahlterArchiveintrag()
    {
        return this.geweahlterArchiveintrag;
    }

    public void setGeweahlterArchiveintrag(Archiveintrag geweahlterArchiveintrag)
    {
        this.geweahlterArchiveintrag = geweahlterArchiveintrag;
    }

    public void buttonAction(Archiveintrag eintrag)
    {
        this.setGeweahlterArchiveintrag(eintrag);
        this.addMessage("Archiveintrag vom " + eintrag.getErstellungsdatum() + " ausgewählt!");
    }

    public List<Archiveintrag> getArchiveintraege()
    {
        List<Archiveintrag> alleEintraege = new ArrayList<>(this.dataBean.getFiltergruppe().getArchiveintraege());

        return alleEintraege; // TODO Filtere Einträge
    }

    private void addMessage(String summary)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
