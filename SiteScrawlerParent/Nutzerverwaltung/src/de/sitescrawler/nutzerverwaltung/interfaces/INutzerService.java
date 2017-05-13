package de.sitescrawler.nutzerverwaltung.interfaces;

import java.util.List;

import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.jpa.Firma;
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
     * Gibt eine Liste aller Administratoren von SiteScrawler.de zurück.
     * @return alle Administratoren von SiteScrawler.de
     */
	List<Nutzer> getAlleAdministratoren();
	
    /**
     * Gibt zurück, ob eine Email bereits in verfügbar ist.
     *
     * @return ob die Email bereits in Verwendung ist.
     */
    boolean isEmailVerfuegbar(String email);

    /**
     * Registriert einen neuen Nutzer, schickt dem Nutzer einen Best�tigungslink zu.
     *
     * @param nutzer
     *            Anzulegender Nutzer.
     * @throws ServiceUnavailableException
     */
    void registrieren(Nutzer nutzer) throws ServiceUnavailableException;
    
    /**
     * Registriert einen neuen Nutzer im Namen einer Firma, schickt dem Nutzer einen Bestätigungslink zu.
     *
     * @param nutzer
     *            Anzulegender Nutzer.
     * @throws ServiceUnavailableException
     */
    void registrieren(Nutzer nutzer, Firma firma) throws ServiceUnavailableException;

    /**
     * Persistiert einen neuen Nutzer in der Datenbank
     *
     * @param nutzer
     */
    void nutzerSpeichern(Nutzer nutzer);

    /**
     * Updated einen Nutzer in der Datenbank
     *
     * @param nutzer
     */
    void nutzerMergen(Nutzer nutzer);

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
     * @param email
     *            Id des Nutzers.
     * @return Nutzer mit der angegebenen Id.
     */
    Nutzer getNutzer(String email);

    /**
     * Schickt dem Nutzer eine Email mit einem Token zu, �ber den der Nutzer sein Passwort zur�cksetzen kann.
     *
     * @param email
     *            Adresse die mit dem Konto des Nutzers verkn�pft ist.
     * @param nutzername
     *            Nutzername des Kontos.
     * @throws ServiceUnavailableException
     */
    void passwortZuruecksetzen(Nutzer nutzer) throws ServiceUnavailableException;

    /**
     * Setzt ein neues Passwort f�r einen Nutzer der zuvor �ber die passwortZuruecksetzen funktion einen Token generiert
     * hat.
     *
     * @param token
     *            Zuvor generierter Token.
     * @param neuesPasswort
     *            Neues Passwort.
     */
    @Deprecated
    void neuesPasswortSetzen(String token, String neuesPasswort);

    /**
     * L�scht den Nutzer aus der Datenbank
     *
     * @param nutzer
     */
    void nutzerLoeschen(Nutzer nutzer);

}
