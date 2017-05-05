package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Firmenrolle;
import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.nutzerverwaltung.NutzerDatenServiceDummy;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.utility.DateUtils;

@SessionScoped
@Named("data")
public class DataBean implements Serializable
{

    private static final long  serialVersionUID = 1L;

    private Nutzer             nutzer;

    @Inject
    private ProjectConfig      config;
    
	    //@Inject
	    private INutzerDatenService nutzerDatenService = new NutzerDatenServiceDummy();

    private Filterprofilgruppe filtergruppe;

    public DataBean()
    { 
    	
    }

    @PostConstruct
    void init()
    {
    	this.nutzer = nutzerDatenService.getNutzer();
    	filtergruppe = new ArrayList<Filterprofilgruppe>(nutzer.getFilterprofilgruppen()).get(0);
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
