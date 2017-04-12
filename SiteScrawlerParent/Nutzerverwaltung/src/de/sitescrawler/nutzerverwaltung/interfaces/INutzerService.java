package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

/**
 * Service, der Nutzerdaten bereitstellt und deren Rolle ver�ndert.
 * 
 * @author konrad mueller
 *
 */
public interface INutzerService
{

    /**
     * Gibt zur�ck, ob eine Email bereits in verf�gbar ist.
     * 
     * @return ob die Email bereits in Verwendung ist.
     */
    boolean isEmailVerfuegbar(String email);

    /**
     * Registriert einen neuen Nutzer, schickt dem Nutzer einen Best�tigungslink zu.
     * 
     * @param nutzer
     *            Anzulegender Nutzer.
     */
    void registrieren(Nutzer nutzer);

    /**
     * Brauchen wir mmn nicht
     * 
     * @param nutzer
     */
    void nutzerSpeichern(Nutzer nutzer);

    /**
     * Warum sollten wir zur Laufzeit rollen anlegen?
     * 
     * @param rolle
     */
    void rolleAnlegen(Rolle rolle);

    /**
     * Gibt einen Nutzer anhand seiner id zur�ck.
     * 
     * @param uid
     *            Id des Nutzers.
     * @return Nutzer mit der angegebenen Id.
     */
    Nutzer getNutzer(String uid);
}
