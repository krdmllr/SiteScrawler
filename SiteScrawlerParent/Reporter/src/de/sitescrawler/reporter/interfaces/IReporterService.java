package de.sitescrawler.reporter.interfaces;

import java.time.LocalDateTime;

import de.sitescrawler.jpa.Filterprofilgruppe;

public interface IReporterService {

	/**
	 * Generiert alle reports die in den aktuellen Shedule Zeitraum fallen.
	 */
	void generiereReports(LocalDateTime zeitpunkt);
	
	/**
	 * Generiert einen Report für die gegebene Filterprofilgruppe.
	 * @param profilgruppe Filterprofilgruppe fuer die der Report erstellt wird.
	 */
	void generiereManuellenReport(Filterprofilgruppe profilgruppe);
}
