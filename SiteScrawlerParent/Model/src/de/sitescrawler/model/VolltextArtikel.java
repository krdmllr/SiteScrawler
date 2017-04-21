package de.sitescrawler.model;

import java.util.ArrayList;
import java.util.List;

public class VolltextArtikel
{
    // private String ueberschrift = "not set";
    // private String zusammenfassung = "not set";
    private List<String> artikelAbsaetze;
    private List<String> tags;

    public VolltextArtikel()
    {
        // this.ueberschrift = "not set";
        // this.zusammenfassung = "not set";
        this.artikelAbsaetze = new ArrayList<>();
        this.tags = new ArrayList<>();

    }

    // public String getUeberschrift()
    // {
    // return this.ueberschrift;
    // }
    //
    // public void setUeberschrift(String ueberschrift)
    // {
    // this.ueberschrift = ueberschrift;
    // }
    //
    // public String getZusammenfassung()
    // {
    // return this.zusammenfassung;
    // }
    //
    // public void setZusammenfassung(String zusammenfassung)
    // {
    // this.zusammenfassung = zusammenfassung;
    // }

    public List<String> getArtikelAbsaetze()
    {
        return this.artikelAbsaetze;
    }

    public List<String> getTags()
    {
        return this.tags;
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return this.artikelAbsaetze.get(1);
    }

}
