package de.sitescrawler.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;

import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Intervall;

public class FiltergruppenZeitpunktCheckUtil {

	private Filterprofilgruppe filtergruppe;
	private LocalDateTime zeitpunkt;
	
	public FiltergruppenZeitpunktCheckUtil(Filterprofilgruppe filtergruppe, LocalDateTime zeitpunkt) {
		super();
		this.filtergruppe = filtergruppe;
		this.zeitpunkt = zeitpunkt;
	}

	/**
	 * Überprüft, ob ausgehend vom letzten erstellungs Datum, ein neuer Archiveintrag erstellt werden soll.
	 * @return ob ein ein Archiveintrag erstellt werden soll.
	 */
	public boolean sollArchiveintragErstelltWerden(){
		LocalDateTime letzteErstellung = DateUtils.asLocalDateTime(filtergruppe.getLetzteerstellung());
		Intervall intervall = filtergruppe.getIntervall(); 
		
		if(letzteErstellung == zeitpunkt) return false;
		 
		switch(intervall.getZeitIntervall()){ 
		case MONATLICH:
			return einMonatVergangen(letzteErstellung);
		case TAEGLICH:
			return true; 
		case WOECHENTLICH:
			return eineWocheVergangen(letzteErstellung);
		default:
			return false;
		}
	}
	
	/**
	 * Überprüft, ob seit dem gegebenen Datum mindestens ein Monat vergangen ist und der gleiche Tag ist.
	 * @param letztesDatum Datum, das mit dem aktuellen Datum verglichen wird.
	 * @return ob seit dem gegebenen Datum mindestens ein Monat vergangen ist und der gleiche Tag ist.
	 */
	private boolean einMonatVergangen(LocalDateTime letztesDatum){
		int letzterMonat = letztesDatum.getMonthValue();
		int aktuellerMonat = zeitpunkt.getMonthValue();
		
		//Überprüfe ob aktueller Monat > letzter Monat
		boolean monatStimmt = false;
		
		if(letztesDatum.getYear() < zeitpunkt.getYear()) 
			monatStimmt = true;
		else if(letzterMonat == aktuellerMonat - 1)
			monatStimmt = true;
		
		return monatStimmt && letztesDatum.getDayOfMonth() == zeitpunkt.getDayOfMonth(); 
	}

	/**
	 * Überprüft, ob seit dem angegebenen Datum 7 Tage oder mehr vergangen sind.
	 * @param letztesDatum Datum, das mit dem aktuellen Datum verglichen wird.
	 * @return ob seit dem angegebenen Datum 7 Tage oder mehr vergangen sind.
	 */
	private boolean eineWocheVergangen(LocalDateTime letztesDatum){
		 LocalDate letztesDatumOhneZeit = letztesDatum.toLocalDate();
		 LocalDate aktuellesDatumOhneZeit = zeitpunkt.toLocalDate();
		 
		 return letztesDatumOhneZeit.plusDays(7) == aktuellesDatumOhneZeit;
	}
}
