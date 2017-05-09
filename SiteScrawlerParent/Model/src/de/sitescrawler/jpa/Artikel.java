package de.sitescrawler.jpa;
// Generated 02.05.2017 16:40:27 by Hibernate Tools 5.2.0.CR1

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Artikelsolr generated by hbm2java
 */
@Entity
public class Artikel implements java.io.Serializable
{
    private static final long  serialVersionUID = 1L;
    private Set<Archiveintrag> archiveintraege  = new HashSet<>(0);
    @Field("id")
    private String             solrid;
    @Field("erstellungsdatum")
    private String             solrdatum;
    @Field
    private String             autor;
    @Field
    private String             titel;
    @Field
    private String             beschreibung;
    @Field
    private String             link;
    @Field
    private List<String>       absaetzeArtikel  = new ArrayList<>();
    private Date               erstellungsdatum;
    private Quelle             quelle;

    public Artikel()
    {
    }

    public Artikel(Set<Archiveintrag> archiveintraege, String solrid)
    {
        this.archiveintraege = archiveintraege;
        this.solrid = solrid;
    }

    public Artikel(Date erstellungsdatum, String autor, String titel, String beschreibung, String link)
    {
        super();
        this.erstellungsdatum = erstellungsdatum;
        this.autor = autor;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.link = link;
    }

    public Artikel(Date erstellungsdatum, String autor, String titel, String beschreibung, String link, List<String> absaetzeArtikel)
    {
        super();
        this.erstellungsdatum = erstellungsdatum;
        this.autor = autor;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.link = link;
        this.absaetzeArtikel = absaetzeArtikel;
    }

    public Artikel(Set<Archiveintrag> archiveintraege, String solrid, Date erstellungsdatum, String autor, String titel, String beschreibung, String link,
                   List<String> absaetzeArtikel)
    {
        this.archiveintraege = archiveintraege;
        this.solrid = solrid;
        this.erstellungsdatum = erstellungsdatum;
        this.autor = autor;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.link = link;
        this.absaetzeArtikel = absaetzeArtikel;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinTable(name = "Archiveintrag_beinhaltet_Artikel", joinColumns = { @JoinColumn(name = "Artikel_solrid", nullable = false, updatable = false) },
               inverseJoinColumns = { @JoinColumn(name = "Archiveintrag_archiveintragid", nullable = false, updatable = false) })
    public Set<Archiveintrag> getArchiveintraege()
    {
        return this.archiveintraege;
    }

    public void setArchiveintraege(Set<Archiveintrag> archiveintraege)
    {
        this.archiveintraege = archiveintraege;
    }

    @Id

    @Column(name = "solrid", unique = true, nullable = false)
    public String getSolrid()
    {
        return this.solrid;
    }

    public void setSolrid(String solrid)
    {
        this.solrid = solrid;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "Quelle_qid", nullable = false, insertable = false, updatable = false)
    public Quelle getQuelle()
    {
        return this.quelle;
    }

    public void setQuelle(Quelle quelle)
    {
        this.quelle = quelle;
    }

    // Unmanaged

    @Transient
    public Date getErstellungsdatum()
    {
        return this.erstellungsdatum;
    }

    public void setErstellungsdatum(Date erstellungsdatum)
    {
        this.erstellungsdatum = erstellungsdatum;
    }

    @Transient
    public String getAutor()
    {
        return this.autor;
    }

    public void setAutor(String autor)
    {
        this.autor = autor;
    }

    @Transient
    public String getTitel()
    {
        return this.titel;
    }

    public void setTitel(String titel)
    {
        this.titel = titel;
    }

    @Transient
    public String getBeschreibung()
    {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }

    @Transient
    public String getLink()
    {
        return this.link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    @Transient
    public List<String> getAbsaetzeArtikel()
    {
        return this.absaetzeArtikel;
    }

    public void setAbsaetzeArtikel(List<String> absaetzeArtikel)
    {
        this.absaetzeArtikel = absaetzeArtikel;
    }

    @Transient
    public String getSolrdatum()
    {
        return this.solrdatum;
    }

    public void setSolrdatum(String solrdatum)
    {
        this.solrdatum = solrdatum;
    }

}
