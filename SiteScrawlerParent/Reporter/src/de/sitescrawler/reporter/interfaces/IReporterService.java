package de.sitescrawler.reporter.interfaces;

import java.time.LocalDateTime;

public interface IReporterService {

	/**
	 * Generiert alle reports die in den aktuellen Shedule Zeitraum fallen.
	 */
	void generiereReports(LocalDateTime zeitpunkt);
}
