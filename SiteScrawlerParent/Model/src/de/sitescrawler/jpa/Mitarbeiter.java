package de.sitescrawler.jpa;
// Generated 02.05.2017 16:40:27 by Hibernate Tools 5.2.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import de.sitescrawler.model.Firmenrolle;

/**
 * Mitarbeiter generated by hbm2java
 */
@Entity
public class Mitarbeiter implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private MitarbeiterId     id;
    private Firma             firma;
    private Nutzer            nutzer;
    private Firmenrolle       firmenrolle;

    public Mitarbeiter()
    {
    }

    public Mitarbeiter(MitarbeiterId id, Firma firma, Nutzer nutzer, Firmenrolle firmenrolle)
    {
        this.id = id;
        this.firma = firma;
        this.nutzer = nutzer;
        this.firmenrolle = firmenrolle;
    }

    @EmbeddedId

    @AttributeOverrides({ @AttributeOverride(name = "nutzerFiltermanagerIdentifikation",
                                             column = @Column(name = "Nutzer_Filtermanager_identifikation", nullable = false, length = 45)),
                          @AttributeOverride(name = "firmaFiltermanagerIdentifikation",
                                             column = @Column(name = "Firma_Filtermanager_identifikation", nullable = false, length = 45)) })
    public MitarbeiterId getId()
    {
        return this.id;
    }

    public void setId(MitarbeiterId id)
    {
        this.id = id;
    }

    // UNMAPPED

    @Transient
    public boolean isAdmin()
    {
        return this.getFirmenrolle() == Firmenrolle.Administrator;
    }

    public void macheZuAdmin()
    {
        // TODO in Datenbank ablegen
        this.setFirmenrolle(Firmenrolle.Administrator);
    }

    public void macheZuNutzer()
    {
        // TODO in Datenbank ablegen
        this.setFirmenrolle(Firmenrolle.Mitarbeiter);
    }

    // MAPPED

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "Firma_Filtermanager_identifikation", nullable = false, insertable = false, updatable = false)
    public Firma getFirma()
    {
        return this.firma;
    }

    public void setFirma(Firma firma)
    {
        this.firma = firma;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "Nutzer_Filtermanager_identifikation", nullable = false, insertable = false, updatable = false)
    public Nutzer getNutzer()
    {
        return this.nutzer;
    }

    public void setNutzer(Nutzer nutzer)
    {
        this.nutzer = nutzer;
    }

    @Column(name = "firmenrolle", nullable = false, length = 45)
    @Enumerated(EnumType.STRING)
    public Firmenrolle getFirmenrolle()
    {
        return this.firmenrolle;
    }

    public void setFirmenrolle(Firmenrolle firmenrolle)
    {
        this.firmenrolle = firmenrolle;
    }

}
