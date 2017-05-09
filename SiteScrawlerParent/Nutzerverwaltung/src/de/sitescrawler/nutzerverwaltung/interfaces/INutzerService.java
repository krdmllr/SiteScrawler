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
     * Gibt einen Nutzer anhand seiner id zur�ck. Der zur�ckgegebene Nutzer beinhaltet alle f�r ihn relevanten Daten wie
     * seine Filtergruppen, in denen auch alle Felder gef�llt sind.
     *
     * @param uid
     *            Id des Nutzers.
     * @return Nutzer mit der angegebenen Id.
     */
    Nutzer getNutzer(String uid);

    /**
     * Schickt dem Nutzer eine Email mit einem Token zu, �ber den der Nutzer sein Passwort zur�cksetzen kann.
     *
     * @param email
     *            Adresse die mit dem Konto des Nutzers verkn�pft ist.
     * @param nutzername
     *            Nutzername des Kontos.
     */
    void passwortZuruecksetzen(String email, String nutzername);

    /**
     * Setzt ein neues Passwort f�r einen Nutzer der zuvor �ber die passwortZuruecksetzen funktion einen Token generiert
     * hat.
     *
     * @param token
     *            Zuvor generierter Token.
     * @param neuesPasswort
     *            Neues Passwort.
     */
    void neuesPasswortSetzen(String token, String neuesPasswort);
}
