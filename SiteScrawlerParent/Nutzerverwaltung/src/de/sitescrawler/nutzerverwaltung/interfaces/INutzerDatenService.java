package de.sitescrawler.nutzerverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;

/**
 * Service, der den Nutzer und seine Daten der aktuellen Session zurückgibt
 *
 * @author Tobias Berner
 *
 */
public interface INutzerDatenService
{

    /**
     * Gibt aktuell angemeldeten Nutzer mit seinen Daten zurück.
     *
     * @return Nutzer mit seinen Daten.
     */
    public Nutzer getNutzer();

    /**
     * Ermittelt der uid bei der Anmeldung die Nutzerdaten und gibt sie zur Verwaltung.
     *
     * @param email
     *            Benutzername.
     */
    public void setNutzer(String email);

    /**
     * Ändert die Email Adresse des aktuellen Nutzers auf die neue Adresse.
     *
     * @param neueEmailAdresse
     *            Neue Email Adresse.
     * @param passwort
     *            Aktuelles Passwort zur Validierung.
     */
    void aendereEmailAdresse(String neueEmailAdresse, String passwort);

    /**
     * Aendert das Passwort auf das neue Passwort.
     *
     * @param neuesPasswort
     *            Das neue Passwort.
     * @param altesPasswort
     *            Aktuelles Passwort zur Validierung.
     */
    void aenderePasswort(String neuesPasswort, String altesPasswort);

    /**
     * Löscht den Account des aktuellen Nutzers.
     *
     * @param passwort
     *            Aktuelles Passwort zur validierung.
     */
    void loescheNutzer(String passwort);

    /**
     * Überprüft das angegebene passwort
     * 
     * @param passwort
     * @return
     */
    boolean verifizierePasswort(String passwort);

}
