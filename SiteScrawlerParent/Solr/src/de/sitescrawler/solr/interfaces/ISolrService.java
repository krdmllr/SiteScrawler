package de.sitescrawler.solr.interfaces;

import java.util.List;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;

public interface ISolrService
{

    /**
     * Fügt Solr einen neuen Artikel hinzu.
     *
     * @param artikel
     */
    void addArtikel(List<Artikel> artikel);

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
     * Sucht die passenden Artikel zur solr id.
     *
     * @param id
     *            des Artikels
     * @return Liste der gefundenen Artikel
     */
    Artikel sucheArtikelMitLink(String id);
    
    /**
     * Komplettieret den Artikel mit Daten aus Solr basierend auf dem bereits im Artikel hinterlegten Link.
     * @param artikel
     */
    void komplettiereArtikel(Artikel artikel);

    /**
     * Löscht alle Artikel (Warum?).
     */
    void clearSolr();

    List<Artikel> getAlleArtikel();
}
