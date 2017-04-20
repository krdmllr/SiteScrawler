package de.sitescrawler.solr.interfaces;

import java.util.List;

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.Filteprofil;

public interface ISolrService
{

    /**
     * Fügt Solr einen neuen Artikel hinzu.
     * 
     * @param artikel
     */
    void addArtikel(Artikel artikel);

    /**
     * Sucht passende Artikel zu einer Liste von Filterprofilen.
     * 
     * @param filterprofile
     *            Filterprofil nach dem die Suche gefiltert werden soll.
     * @return Liste der Artikel, die dem Filterprofil entsprechen.
     */
    List<Artikel> sucheArtikel(List<Filteprofil> filterprofile);
    
    /**
     * Sucht passende Artikel zu einem gegebenen FilterProfil.
     * 
     * @param filterProfil
     *            Filterprofil nach dem die Suche gefiltert werden soll.
     * @return Liste der Artikel, die dem Filterprofil entsprechen.
     */
    List<Artikel> sucheArtikel(Filteprofil filterprofil);

    /**
     * Löscht alle Artikel (Warum?).
     */
    void clearSolr();
}
