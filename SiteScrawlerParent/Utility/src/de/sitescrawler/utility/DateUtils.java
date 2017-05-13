package de.sitescrawler.utility;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.DateFormatter;

/**
 * 
 * @author robin
 * Util Methoden
 */
public class DateUtils
{
    public static final String DATE_PATTERN = "EE dd.MM.yyyy";
    
    public static DateTimeFormatter getDateFormatter(){ 
    	DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();
    	
    	return formatBuilder.toFormatter();
    }

    public static Date asDate(LocalDate localDate)
    {
        LocalDateTime atStartOfDay = localDate.atStartOfDay();
        ZoneId systemDefault = ZoneId.systemDefault();
        ZonedDateTime atZone = atStartOfDay.atZone(systemDefault);
        Instant instant = atZone.toInstant();
        Date from = Date.from(instant);
        return from;
    }

    public static Date asDate(LocalDateTime localDateTime)
    {
        return localDateTime == null ? null : Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date)
    {
        return date == null ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date)
    {
        return date == null ? null : Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long businessDaysBetween(LocalDate start, LocalDate stop)
    {
        return DateUtils.businessDaysBetween(start, stop, new ArrayList<LocalDate>());
    }

    public static long businessDaysBetween(LocalDate start, LocalDate stop, List<LocalDate> holidays)
    {
        if (start.isAfter(stop))
        {
            return -1;
        }
        long count = 0;
        while (start.isBefore(stop) || start.isEqual(stop))
        {
            DayOfWeek dayOfWeek = start.getDayOfWeek();
            if (!dayOfWeek.equals(DayOfWeek.SATURDAY) && !dayOfWeek.equals(DayOfWeek.SUNDAY) && !DateUtils.containsDate(holidays, start))
            {
                count++;
            }
            start = start.plusDays(1);
        }
        return count;

    }

    private static boolean containsDate(List<LocalDate> list, LocalDate date)
    {
        return list.stream().anyMatch(listDate -> listDate.isEqual(date));
    }
    
    /**
	 * Berechnet die aktuelle Zeit und rundet die Minuten auf die nähste halbe Stunde/volle Stunde.
	 * @return
	 */
	public static LocalDateTime rundeZeitpunkt(LocalDateTime aktuelleZeit){		
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
