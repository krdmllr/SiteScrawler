package de.sitescrawler.jpa;
// Generated 02.05.2017 16:40:27 by Hibernate Tools 5.2.0.CR1

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Filtermanager generated by hbm2java
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Filtermanager implements java.io.Serializable
{

    private static final long       serialVersionUID    = 1L;
    private String                  identifikation;
    private int                     maxfiltergruppe;
    private Set<Filterprofil>       filterprofile       = new HashSet<>(0);
    private Set<Filterprofilgruppe> filterprofilgruppen = new HashSet<>(0);

    public Filtermanager()
    {
    }

    public Filtermanager(String identifikation, int maxfiltergruppe)
    {
        this.identifikation = identifikation;
        this.maxfiltergruppe = maxfiltergruppe;
    }

    public Filtermanager(String identifikation, int maxfiltergruppe, Set<Filterprofil> filterprofile, Set<Filterprofilgruppe> filterprofilgruppen)
    {
        this.identifikation = identifikation;
        this.maxfiltergruppe = maxfiltergruppe;
        this.filterprofile = filterprofile;
        this.filterprofilgruppen = filterprofilgruppen;
    }

    @Id

    @Column(name = "identifikation", unique = true, nullable = false, length = 45)
    public String getIdentifikation()
    {
        return this.identifikation;
    }

    public void setIdentifikation(String identifikation)
    {
        this.identifikation = identifikation;
    }

    @Column(name = "maxfiltergruppe", nullable = false)
    public int getMaxfiltergruppe()
    {
        return this.maxfiltergruppe;
    }

    public void setMaxfiltergruppe(int maxfiltergruppe)
    {
        this.maxfiltergruppe = maxfiltergruppe;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "filtermanager", cascade = { CascadeType.ALL })
    public Set<Filterprofil> getFilterprofile()
    {
        return this.filterprofile;
    }

    @Transient
    public List<Filterprofil> getFilterprofileList()
    {
        return new ArrayList<>();
    }

    public void setFilterprofile(Set<Filterprofil> filterprofile)
    {
        this.filterprofile = filterprofile;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "filtermanager", cascade = { CascadeType.ALL })
    public Set<Filterprofilgruppe> getFilterprofilgruppen()
    {
        return this.filterprofilgruppen;
    }

    public void setFilterprofilgruppen(Set<Filterprofilgruppe> filterprofilgruppen)
    {
        this.filterprofilgruppen = filterprofilgruppen;
    }

}
