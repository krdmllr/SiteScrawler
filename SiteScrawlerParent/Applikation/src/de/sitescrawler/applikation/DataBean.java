package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;

/**
 *
 * @author robin Data Bean
 */
@SessionScoped
@Named("data")
public class DataBean implements Serializable
{

    private static final long   serialVersionUID = 1L;

    private Nutzer              nutzer;

    @Inject
    private ProjectConfig       config;

    @Inject
    private ArchivBean          archiv;

    @Inject
    private INutzerDatenService nutzerDatenService;

    private Filterprofilgruppe  filtergruppe;

    public DataBean()
    {

    }

    @PostConstruct
    void init()
    {
        this.nutzer = this.nutzerDatenService.getNutzer();
        if (!this.nutzer.getFilterprofilgruppen().isEmpty())
        {
            this.filtergruppe = new ArrayList<>(this.nutzer.getFilterprofilgruppen()).get(0);
        }
    }

    public Nutzer getNutzer()
    {
        return this.nutzer;
    }

    public void setNutzer(Nutzer nutzer)
    {
        this.nutzer = nutzer;
    }

    public Filterprofilgruppe getFiltergruppe()
    {
        return this.filtergruppe;
    }

    public void setFiltergruppe(Filterprofilgruppe filtergruppe)
    {

        this.filtergruppe = filtergruppe;
        if (filtergruppe.getArchiveintraege().size() > 0)
        {
            this.archiv.setGeweahlterArchiveintrag((Archiveintrag) filtergruppe.getArchiveintraege().toArray()[0]);
        }
    }

    public List<Filterprofilgruppe> getFiltergruppen()
    {
        if (this.nutzer == null)
        {
            return null;
        }

        List<Filterprofilgruppe> alleFiltergruppen = new ArrayList<>();
        alleFiltergruppen.addAll(this.nutzer.getFilterprofilgruppen());

        for (Firma f : this.nutzer.getFirmen())
        {
            for (Filterprofilgruppe ffg : f.getFilterprofilgruppen())
            {
                for (Nutzer ma : ffg.getEmpfaenger())
                {
                    if (ma.equals(this.nutzer))
                    {
                        alleFiltergruppen.add(ffg);
                    }
                }
            }
        }

        return alleFiltergruppen;
    }

    public ProjectConfig getConfig()
    {
        return this.config;
    }
}
