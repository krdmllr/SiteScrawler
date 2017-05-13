package de.sitescrawler.applikation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import de.sitescrawler.formatierung.IFormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenManager;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
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
    private final static Logger           LOGGER           = Logger.getLogger("de.sitescrawler.logger");

    private static final long             serialVersionUID = 1L;

    @Inject
    private DataBean                      dataBean;

    @Inject
    private IReportService                reporterService;

    @Inject
    private IFiltergruppenManager         filtergruppenManager;

    @Inject
    private IFiltergruppenZugriffsManager filtergruppenZugriffsManager;

    @Inject
    private IFormatiererService           formatiererService;

    private Archiveintrag                 geweahlterArchiveintrag;
    private Artikel                       geweahlterArtikel;

    private Date                          abZeitpunkt      = new Date();
    private Date                          bisZeitpunkt     = new Date();

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

        this.filtergruppenZugriffsManager.loescheArchiveintrag(this.geweahlterArchiveintrag);
        this.dataBean.getFiltergruppe().getArchiveintraege().remove(this.geweahlterArchiveintrag);
        ArchivBean.LOGGER.log(Level.INFO, "Archiveintrag vom " + this.geweahlterArchiveintrag.getErstellungsdatum() + " wurde gelöscht.");
        this.setzeErstenArchiveintrag();
    }

    /**
     * Exportiert das PDF des gewählten Archiveintrages und bietet es dem Nutzer als Download an.
     */
    public StreamedContent exportierePdf()
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
                return this.downloadPdf(pdfData);
            }
            catch (Exception ex)
            {
                // TODO Auto-generated catch block
                ArchivBean.LOGGER.log(Level.SEVERE, "PDF Download fehlgeschlagen.", ex);
            }
        }
        return null;
    }

    public StreamedContent downloadPdf(ByteArrayOutputStream pdfData)
    {
        ByteArrayInputStream pdfIn = new ByteArrayInputStream(pdfData.toByteArray());
        DefaultStreamedContent pdfStream = new DefaultStreamedContent(pdfIn, "application/pdf",
                                                                      "Pressespiegel_" + this.getGeweahlterArchiveintrag().getErstellungsdatum() + ".pdf");
        ArchivBean.LOGGER.info("PDF zum Download bereit.");
        return pdfStream;

    }

    public void manuellCrawlen()
    {
        ArchivBean.LOGGER.info("Crawle");
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
