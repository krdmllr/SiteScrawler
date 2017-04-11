package de.sitescrawler.solr.interfaces;

import java.util.List;

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterProfil;

public interface ISolrService {
	
	/**
	 * Fügt Solr einen neuen Artikel hinzu.
	 * @param artikel
	 */
	void addArtikel(Artikel artikel);

	/**
	 * Sucht passende Artikel zu einem gegebenen FilterProfil.
	 * @param filterProfil Filterprofil nach dem die Suche gefiltert werden soll.
	 * @return Liste der Artikel, die dem Filterprofil entsprechen.
	 */
	List<Artikel> sucheArtikel(FilterProfil filterProfil);

	/**
	 * Löscht alle Artikel (Warum?).
	 */
	void clearSolr();
}
