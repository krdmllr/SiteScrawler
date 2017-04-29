package de.sitescrawler.solr.interfaces;

import java.util.List;

import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.model.Artikel; 

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
    List<Artikel> sucheArtikel(List<Filterprofil> filterprofile);
    
    /**
     * Sucht passende Artikel zu einem gegebenen FilterProfil.
     * 
     * @param filterProfil
     *            Filterprofil nach dem die Suche gefiltert werden soll.
     * @return Liste der Artikel, die dem Filterprofil entsprechen.
     */
    List<Artikel> sucheArtikel(Filterprofil filterprofil);

    /**
     * Löscht alle Artikel (Warum?).
     */
    void clearSolr();
    
    List<Artikel> getAlleArtikel();
}
