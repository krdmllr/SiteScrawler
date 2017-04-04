package de.sitescrawler.model;

import java.util.Date;

public class Artikel
{
    private Date   erstellungsdatum;
    private String autor;
    private String titel;
    private String beschreibung;
    private String link;

    public Artikel()
    {
    }

    public Artikel(Date erstellungsdatum, String autor, String titel, String beschreibung, String link)
    {
        this.erstellungsdatum = erstellungsdatum;
        this.autor = autor;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.link = link;
    }

    public Date getErstellungsdatum()
    {
        return this.erstellungsdatum;
    }

    public void setErstellungsdatum(Date erstellungsdatum)
    {
        this.erstellungsdatum = erstellungsdatum;
    }

    public String getAutor()
    {
        return this.autor;
    }

    public void setAutor(String autor)
    {
        this.autor = autor;
    }

    public String getTitel()
    {
        return this.titel;
    }

    public void setTitel(String titel)
    {
        this.titel = titel;
    }

    public String getBeschreibung()
    {
        return this.beschreibung;
    }

    public void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }

    public String getLink()
    {
        return this.link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    // vergleicht auf autor und titel
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        Artikel other = (Artikel) obj;
        if (this.autor == null)
        {
            if (other.autor != null)
            {
                return false;
            }
        }
        else
            if (!this.autor.equals(other.autor))
            {
                return false;
            }
        if (this.titel == null)
        {
            if (other.titel != null)
            {
                return false;
            }
        }
        else
            if (!this.titel.equals(other.titel))
            {
                return false;
            }
        return true;
    }

}
