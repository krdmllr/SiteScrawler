package de.sitescrawler.exceptions;
 
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;
import de.sitescrawler.model.Firmenrolle;

public class FirmenSecurityException extends Exception{

	public FirmenSecurityException(Nutzer nutzer, Firmenrolle benoetigteRolle, String aktion){ 
		super(nutzer.getGanzenNamen() + " hat nicht die benötigte Rolle " + benoetigteRolle.toString() + " um " + aktion);  
	}
}
