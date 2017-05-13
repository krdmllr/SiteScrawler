package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;

public interface IPasswortService
{

    /**
     * Generiert ein 8stelliges Passwort und weist es dem Nutzer zu.
     * 
     * @param nutzer
     * @return generiertes Passwort
     */
    String setNeuesPasswort(Nutzer nutzer);

}