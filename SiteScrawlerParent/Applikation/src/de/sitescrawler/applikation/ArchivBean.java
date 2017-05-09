package de.sitescrawler.applikation;

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

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenManager;
import de.sitescrawler.report.IReportService; 

@SessionScoped
@Named("archiv")
public class ArchivBean implements Serializable
{
	// Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    private static final long serialVersionUID = 1L;

    @Inject
    private DataBean          dataBean;

    @Inject
    private IReportService  reporterService;
    
    @Inject
    private IFiltergruppenManager filtergruppenManager;

    private Archiveintrag     geweahlterArchiveintrag;
    private Artikel           geweahlterArtikel;

    private Date              abZeitpunkt      = new Date();
    private Date              bisZeitpunkt     = new Date();

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
    	setzeErstenArchiveintrag();
    }
    
    /**
     * Setzt den ersten Archiveintrag der aktuellen Filtergruppe als gewählten Archiveintrag.
     */
    private void setzeErstenArchiveintrag(){ 
        List<Archiveintrag> archiveintraege = getArchiveintraege();
        if (!archiveintraege.isEmpty())
        {
            this.setGeweahlterArchiveintrag(archiveintraege.get(0));
        }
    }
    
    /**
     * Löscht den gewählten Archiveintrag.
     */
    public void loescheArchiveintrag(){ 
    	
    	dataBean.getFiltergruppe().getArchiveintraege().remove(geweahlterArchiveintrag);
    	filtergruppenManager.speichereAenderung(dataBean.getFiltergruppe());
    	LOGGER.log(Level.INFO, "Archiveintrag vom " + geweahlterArchiveintrag.getErstellungsdatum() + " wurde gelöscht.");
    	 
    	setzeErstenArchiveintrag();
    }
    
    /**
     * Exportiert das PDF des gewählten Archiveintrages und bietet es dem Nutzer als Download an.
     */
    public void exportierePdf(){
    	//TODO PDF exportierung und Download implementieren.
    }

    public void manuellCrawlen()
    {
        System.out.println("CRAWLE");
        this.reporterService.generiereManuellenReport(this.dataBean.getFiltergruppe());
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
    
    public List<Archiveintrag> getArchiveintraege(){
    	List<Archiveintrag> alleEintraege = new ArrayList<>(dataBean.getFiltergruppe().getArchiveintraege());
    	
    	
    	return alleEintraege; //TODO Filtere Einträge
    }

    private void addMessage(String summary)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    } 
}
