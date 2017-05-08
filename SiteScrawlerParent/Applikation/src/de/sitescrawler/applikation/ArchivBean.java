package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofilgruppe;
//import de.sitescrawler.reporter.interfaces.IReporterService;

@SessionScoped
@Named("archiv")
public class ArchivBean implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Inject
    private DataBean          dataBean;
    
    //@Inject
    //private IReporterService reporterService;

    private Archiveintrag     geweahlterArchiveintrag;
    private Artikel           geweahlterArtikel;

    private Date              abZeitpunkt      = new Date();
    private Date              bisZeitpunkt     = new Date();

    public ArchivBean()
    {

    }

    @PostConstruct
    void init()
    {
        Filterprofilgruppe filtergruppe = this.dataBean.getFiltergruppe();
        List<Archiveintrag> archiveintraege = new ArrayList<>(filtergruppe.getArchiveintraege());
        if (!archiveintraege.isEmpty())
        {
            this.setGeweahlterArchiveintrag(archiveintraege.get(0));
        }
    }
    
    public void manuellCrawlen(){
    	System.out.println("CRAWLE");
    	//reporterService.generiereManuellenReport(dataBean.getFiltergruppe());
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

    private void addMessage(String summary)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
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
}
