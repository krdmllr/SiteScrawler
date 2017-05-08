package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.Rolle;

/**
 * Service, der Nutzerdaten bereitstellt und deren Rolle verändert.
 *
 * @author konrad mueller
 *
 */
public interface INutzerService
{

    /**
     * Gibt zurück, ob eine Email bereits in verfügbar ist.
     *
     * @return ob die Email bereits in Verwendung ist.
     */
    boolean isEmailVerfuegbar(String email);

    /**
     * Registriert einen neuen Nutzer, schickt dem Nutzer einen Bestätigungslink zu.
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
     * Gibt einen Nutzer anhand seiner id zurück. Der zurückgegebene Nutzer beinhaltet alle für ihn relevanten Daten wie
     * seine Filtergruppen, in denen auch alle Felder gefüllt sind.
     *
     * @param uid
     *            Id des Nutzers.
     * @return Nutzer mit der angegebenen Id.
     */
    Nutzer

                    getNutzer(String uid);

    /**
     * Schickt dem Nutzer eine Email mit einem Token zu, über den der Nutzer sein Passwort zurücksetzen kann.
     *
     * @param email
     *            Adresse die mit dem Konto des Nutzers verknüpft ist.
     * @param nutzername
     *            Nutzername des Kontos.
     */
    void passwortZuruecksetzen(String email, String nutzername);

    /**
     * Setzt ein neues Passwort für einen Nutzer der zuvor über die passwortZuruecksetzen funktion einen Token generiert
     * hat.
     *
     * @param token
     *            Zuvor generierter Token.
     * @param neuesPasswort
     *            Neues Passwort.
     */
    void neuesPasswortSetzen(String token, String neuesPasswort);
}
