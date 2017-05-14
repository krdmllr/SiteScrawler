package de.sitescrawler.firmenverwaltung.interfaces;
 
import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer; 
import de.sitescrawler.model.Firmenrolle;

/**
 * Service zum Verwalten von bestehenden Firmen.
 * 
 * @author konrad mueller
 */
public interface IFirmenService
{

    /**
     * Ermittelt ob ein Firmenname noch verf�gbar ist.
     * 
     * @param name
     *            Der zu prüfende Firmenname.
     * @return Ob der name noch verfügbar ist.
     */
    boolean istFirmenMailVerfuegbar(String name);

    /**
     * Beantragt eine neue Firmen Entit�t. Der Antrag wird anschlie�end gepr�ft und manuell freigeschaltet.
     * 
     * @param nutzer
     *            Der Nutzer, welcher die Firma beantragt.
     * @param firmenName
     *            Name der beantragten Firma.
     * @param comment
     *            Kommentar zu Firmenbeantragung.
     * @return Ob der Antrag erfolgreich erstellt wurde.
     */
    boolean FirmaBeantragen(String firmenName, String firmenMail, String kommentar);

    /**
     * L�dt einen Nutzer zur Firma ein.
     * 
     * @param firma
     *            Einladende Firma.
     * @param email
     *            Email des neuen Mitarbeiters.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     * @throws ServiceUnavailableException 
     * @throws Exception 
     */
    void nutzerEinladen(Firma firma, String email, String vorname, String nachname) throws ServiceUnavailableException, Exception;

    //TODO KOMMENTARE
    void bestehendenNutzerEinladen(Nutzer bestehenderNutzer, Firma firma)
			throws Exception;
    
    /**
     * Entfernt einen Nutzer aus der Mitarbeitergruppe einer Firma.
     * 
     * @param firma
     *            Firma von dessen Mitarbeitern der Nutzer entfernt wird.
     * @param zuEntfernenderNutzer
     *            Nutzer der von den Mitarbeitern entfernt wird.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     * @throws NotFoundException 
     * @throws Exception 
     */
    void NutzerEntfernen(Firma firma, Mitarbeiter zuEntfernenderNutzer) throws Exception;

    /**
     * Setzt, ob ein Nutzer Adminstatus f�r eine Firma erhalten soll.
     * 
     * @param firma
     *            Einladende Firma.
     * @param nutzer
     *            Nutzer dessen Status verhindert werden soll.
     * @param rolle
     *            Neue Rolle des Nutzers.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     * @throws NotFoundException 
     * @throws Exception 
     */
    void SetzeNutzerRolle(Firma firma, Mitarbeiter nutzer, Firmenrolle rolle) throws Exception;

    /**
     * Beantragt die Löschung einer Firma, die L�schung muss über einen Bestätigungslink best�tigt werden.
     * 
     * @param firma
     *            Zu löschende Firma.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     */
    void loescheFirma(Firma firma, Nutzer angemeldeterNutzer);
}
