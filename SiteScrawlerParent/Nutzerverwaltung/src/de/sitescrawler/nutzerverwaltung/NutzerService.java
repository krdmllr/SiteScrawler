package de.sitescrawler.nutzerverwaltung;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

public interface NutzerService {

	void registrieren(Nutzer nutzer);
	
	void nutzerSpeichern(Nutzer nutzer);

	void rolleAnlegen(Rolle rolle);

	Nutzer getNutzer(String uid);

}
