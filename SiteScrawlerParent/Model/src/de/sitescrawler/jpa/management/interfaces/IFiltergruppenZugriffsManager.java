package de.sitescrawler.jpa.management.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Filterprofilgruppe;

public interface IFiltergruppenZugriffsManager {
	
	/**
	 * Gibt alle Filtergruppen zur�ck, die die Archiveintragsgenerierung zum gegebenen Zeitpunkt angestellt haben.
	 * Zeitpunkt wird exakt verglichen (Auf Minute).
	 * @param empfangszeitpunkt exakter Empfangszeitpunkt (auf Minute) mit dem verglichen werden soll.
	 * @return Liste aller zutreffenden Filterprofilgruppen.
	 */
	public List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuZeitpunkt(LocalDateTime empfangszeitpunkt);
	
	/**
	 * Speichert einen neune Archiveintrag in die Filtergruppe aus dem er erstellt wurde ab.
	 */
	public void speicherArchiveintrag(Archiveintrag archiveintrag);
	
	/**
	 * Löscht den Archiveintrag.
	 * @param archiveintrag
	 */
	public void loescheArchiveintrag(Archiveintrag archiveintrag);
}
