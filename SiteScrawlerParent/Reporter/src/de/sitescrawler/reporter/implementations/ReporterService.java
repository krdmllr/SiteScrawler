package de.sitescrawler.reporter.implementations;

import java.time.LocalDateTime; 
import java.util.List; 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.management.IFiltergruppenZugriffsManager;
import de.sitescrawler.jpa.management.implementation.FiltergruppenZugriffsManager;
import de.sitescrawler.logger.LoggerSiteScrawler;
import de.sitescrawler.reporter.interfaces.IReporterService; 

public class ReporterService implements IReporterService {
	 
	@Inject
	IFiltergruppenZugriffsManager filtergruppenZugriff;
	ExecutorService      threadPool = Executors.newFixedThreadPool(5);
	
	public ReporterService(){
		if(filtergruppenZugriff == null)
			filtergruppenZugriff = new FiltergruppenZugriffsManager();
	}
	
	public void generiereReports(LocalDateTime zeitpunkt) { 
		LocalDateTime korrigierteAktuelleZeit = rundeZeitpunkt(zeitpunkt);
		
		List<Filterprofilgruppe> gruppen = getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(korrigierteAktuelleZeit); 
		
		for(Filterprofilgruppe fg : gruppen){
			Runnable run = new ArchiveintragErstellen(fg, korrigierteAktuelleZeit);
            this.threadPool.submit(run);
		}
	}   
	
	private List<Filterprofilgruppe> getFiltergruppeMitEmpfangZuAktuellemZeitpunkt(LocalDateTime zeitpunkt){
		return filtergruppenZugriff.getFiltergruppeMitEmpfangZuZeitpunkt(zeitpunkt); 
	}
	
	/**
	 * Berechnet die aktuelle Zeit und rundet die Minuten auf die nähste halbe Stunde/volle Stunde.
	 * @return
	 */
	private LocalDateTime rundeZeitpunkt(LocalDateTime aktuelleZeit){		
		//Runde Minute abhängig von der aktuellen Sekunde und setze Sekunde auf 0
		int sekunde = aktuelleZeit.getSecond();
		if(sekunde >= 30)
			aktuelleZeit = aktuelleZeit.withMinute(aktuelleZeit.getMinute()+1);
		
		aktuelleZeit = aktuelleZeit.withSecond(0);
		
		//Runde Minute auf die nächste halbe Stunde/volle Stunde
		int minute = aktuelleZeit.getMinute();
		
		if(minute != 30 && minute != 30){
			
			//Berechne Abstände zu nächsten vollen Stunden/halber Stunde
			int abstandZuLetzterStunde = minute;
			int abstandZuNaechsterStunde = 60 - minute;
			int abstandZuHalberStunde = minute < 30 ? 30 - minute : minute - 30;
			
			//Korrigiere Zeit zu nächster Zeit
			if(abstandZuHalberStunde < abstandZuLetzterStunde && abstandZuHalberStunde < abstandZuNaechsterStunde){
				//Nähster Abstand zu halber Stunde
				aktuelleZeit = aktuelleZeit.withMinute(30);
			}else if(abstandZuLetzterStunde < abstandZuNaechsterStunde){
				//Nähster Abstand zum aktuellen Stunden Anfang
				aktuelleZeit = aktuelleZeit.withMinute(0);
			}
			else
			{
				//Nähster Abstand zu nächster Stunde, zähle auch Stunde hoch
				aktuelleZeit = aktuelleZeit.withMinute(0).withHour(aktuelleZeit.getHour()+1);
			}
		} 
		
		return aktuelleZeit; 
	}
}
