package de.sitescrawler.firmenverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;

/**
 * Service für das erstellen von Firmen.
 * @author konrad mueller
 */
public interface IFirmenErstellungsService {
	
	/**
	 * Ermittelt ob ein Firmenname noch verfügbar ist.
	 * @param name Der zu prüfende Firmenname.
	 * @return Ob der name noch verfügbar ist.
	 */
	boolean IsFirmennameVerfuegbar(String name);
	
	/**
	 * Beantragt eine neue Firmen Entität. Der Antrag wird anschließend geprüft und manuell freigeschaltet.
	 * @param nutzer Der Nutzer, welcher die Firma beantragt.
	 * @param firmenName Name der beantragten Firma.
	 * @param comment Kommentar zu Firmenbeantragung.
	 * @return Ob der Antrag erfolgreich erstellt wurde.
	 */
	boolean FirmaBeantragen(Nutzer nutzer, 
			String firmenName, 
			String comment);
}
