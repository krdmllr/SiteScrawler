package de.sitescrawler.jpa;
// Generated 02.05.2017 16:40:27 by Hibernate Tools 5.2.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * Firma generated by hbm2java
 */
@Entity
public class Firma extends Filtermanager implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String            name;
    private Set<Mitarbeiter>  mitarbeiter      = new HashSet<>(0);

    public Firma()
    {
    }

    public Firma(String name, Set<Mitarbeiter> mitarbeiter)
    {
        this.name = name;
        this.mitarbeiter = mitarbeiter;
    }

    @Column(name = "name")
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "firma")
    public Set<Mitarbeiter> getMitarbeiter()
    {
        return this.mitarbeiter;
    }

    public void setMitarbeiter(Set<Mitarbeiter> mitarbeiter)
    {
        this.mitarbeiter = mitarbeiter;
    }

}
