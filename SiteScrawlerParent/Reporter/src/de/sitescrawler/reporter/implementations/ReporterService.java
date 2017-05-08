package de.sitescrawler.reporter.implementations;

import java.time.LocalDateTime; 
import java.util.List; 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.FiltergruppenZugriffsManager;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager; 
import de.sitescrawler.reporter.interfaces.IReporterService;
import de.sitescrawler.utility.DateUtils; 

@ApplicationScoped
public class ReporterService implements IReporterService {
	 
	@Inject
	IFiltergruppenZugriffsManager filtergruppenZugriff;
	ExecutorService      threadPool = Executors.newFixedThreadPool(5); 
	
	@Inject
	ArchiveintragErstellen archiveintragErstellen;
	
	public void generiereReports(LocalDateTime zeitpunkt) { 
		LocalDateTime korrigierteAktuelleZeit = DateUtils.rundeZeitpunkt(zeitpunkt);
		
		List<Filterprofilgruppe> gruppen = getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(korrigierteAktuelleZeit); 
		
		for(Filterprofilgruppe fg : gruppen){
			archiveintragErstellen.erstelleReport(fg, korrigierteAktuelleZeit);
		}
	}   
	
	private List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(LocalDateTime zeitpunkt){
		return filtergruppenZugriff.getFiltergruppeMitEmpfangZuZeitpunkt(zeitpunkt); 
	}

	public void generiereManuellenReport(Filterprofilgruppe profilgruppe) {
		 
		archiveintragErstellen.erstelleReport(profilgruppe, LocalDateTime.now());
	} 
}
