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
	
	public ReporterService(){
		if(filtergruppenZugriff == null)
			filtergruppenZugriff = new FiltergruppenZugriffsManager();
	}
	
	public void generiereReports(LocalDateTime zeitpunkt) { 
		LocalDateTime korrigierteAktuelleZeit = DateUtils.rundeZeitpunkt(zeitpunkt);
		
		List<Filterprofilgruppe> gruppen = getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(korrigierteAktuelleZeit); 
		
		for(Filterprofilgruppe fg : gruppen){
			Runnable run = new ArchiveintragErstellen(fg, korrigierteAktuelleZeit);
            this.threadPool.submit(run);
		}
	}   
	
	private List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(LocalDateTime zeitpunkt){
		return filtergruppenZugriff.getFiltergruppeMitEmpfangZuZeitpunkt(zeitpunkt); 
	}

	public void generiereManuellenReport(Filterprofilgruppe profilgruppe) {
		ArchiveintragErstellen ersteller = new ArchiveintragErstellen(profilgruppe, LocalDateTime.now()); 
		ersteller.erstelleReport();
	} 
}
