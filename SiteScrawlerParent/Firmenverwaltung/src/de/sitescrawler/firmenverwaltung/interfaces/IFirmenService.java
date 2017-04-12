package de.sitescrawler.firmenverwaltung.interfaces;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Firma;

/**
 * Service zum Verwalten von bestehenden Firmen.
 * 
 * @author konrad mueller
 */
public interface IFirmenService
{

    /**
     * Ermittelt ob ein Firmenname noch verfügbar ist.
     * 
     * @param name
     *            Der zu prüfende Firmenname.
     * @return Ob der name noch verfügbar ist.
     */
    boolean IsFirmennameVerfuegbar(String name);

    /**
     * Beantragt eine neue Firmen Entität. Der Antrag wird anschließend geprüft und manuell freigeschaltet.
     * 
     * @param nutzer
     *            Der Nutzer, welcher die Firma beantragt.
     * @param firmenName
     *            Name der beantragten Firma.
     * @param comment
     *            Kommentar zu Firmenbeantragung.
     * @return Ob der Antrag erfolgreich erstellt wurde.
     */
    boolean FirmaBeantragen(Nutzer nutzer, String firmenName, String comment);

    /**
     * Lädt einen Nutzer zur Firma ein.
     * 
     * @param firma
     *            Einladende Firma.
     * @param email
     *            Email des neuen Mitarbeiters.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     */
    void NutzerEinladen(Firma firma, String email, Nutzer angemeldeterNutzer);

    /**
     * Entfernt einen Nutzer aus der Mitarbeitergruppe einer Firma.
     * 
     * @param firma
     *            Firma von dessen Mitarbeitern der Nutzer entfernt wird.
     * @param zuEntfernenderNutzer
     *            Nutzer der von den Mitarbeitern entfernt wird.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     */
    void NutzerEntfernen(Firma firma, Nutzer zuEntfernenderNutzer, Nutzer angemeldeterNutzer);

    /**
     * Setzt, ob ein Nutzer Adminstatus für eine Firma erhalten soll.
     * 
     * @param firma
     *            Einladende Firma.
     * @param nutzer
     *            Nutzer dessen Status verändert werden soll.
     * @param sollAdminSein
     *            Ob der Nutzer Admin status erhalten soll oder normaler Nutzer sein soll.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     */
    void SetzeNutzerAdminStatus(Firma firma, Nutzer nutzer, boolean sollAdminSein, Nutzer angemeldeterNutzer);

    /**
     * Beantragt die Löschung einer Firma, die Löschung muss über einen Bestätigungslink bestätigt werden.
     * 
     * @param firma
     *            Zu löschende Firma.
     * @param angemeldeterNutzer
     *            Angemeldeter Nutzer von dem die Action ausgeht, muss Firmenadministrator sein.
     */
    void loescheFirma(Firma firma, Nutzer angemeldeterNutzer);
}
