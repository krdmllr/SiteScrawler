package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

/**
 * Service, der Nutzerdaten bereitstellt und deren Rolle verï¿½ndert.
 *
 * @author konrad mueller
 *
 */
public interface INutzerService
{

    /**
     * Gibt zurï¿½ck, ob eine Email bereits in verfï¿½gbar ist.
     *
     * @return ob die Email bereits in Verwendung ist.
     */
    boolean isEmailVerfuegbar(String email);

    /**
     * Registriert einen neuen Nutzer, schickt dem Nutzer einen Bestï¿½tigungslink zu.
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
     * Gibt einen Nutzer anhand seiner id zurï¿½ck. Der zurï¿½ckgegebene Nutzer beinhaltet alle fï¿½r ihn relevanten
     * Daten wie seine Filtergruppen, in denen auch alle Felder gefï¿½llt sind.
     *
     * @param email
     *            Id des Nutzers.
     * @return Nutzer mit der angegebenen Id.
     */
    Nutzer getNutzer(String email);

    /**
     * Schickt dem Nutzer eine Email mit einem Token zu, ï¿½ber den der Nutzer sein Passwort zurï¿½cksetzen kann.
     *
     * @param email
     *            Adresse die mit dem Konto des Nutzers verknï¿½pft ist.
     * @param nutzername
     *            Nutzername des Kontos.
     */
    void passwortZuruecksetzen(String email, String nutzername);

    /**
     * Setzt ein neues Passwort fï¿½r einen Nutzer der zuvor ï¿½ber die passwortZuruecksetzen funktion einen Token
     * generiert hat.
     *
     * @param token
     *            Zuvor generierter Token.
     * @param neuesPasswort
     *            Neues Passwort.
     */
    void neuesPasswortSetzen(String token, String neuesPasswort);

    /**
     * Löscht den Nutzer aus der Datenbank
     * 
     * @param nutzer
     */
    void nutzerLoeschen(Nutzer nutzer);
}
