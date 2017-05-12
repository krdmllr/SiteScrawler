package de.sitescrawler.applikation;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.crawler.interfaces.ICrawlerLaufService;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.jpa.management.interfaces.IQuellenManager;

/**
 *
 * @author robin QuellenBean, alle Methoden zur Quellenverwaltung.
 */
@SessionScoped
@Named("quellen")
public class QuellenBean implements Serializable
{

    private static final long   serialVersionUID = 1L;

    @Inject
    private DataBean            dataBean;

    @Inject
    private IQuellenManager     quellenManager;

    @Inject
    private ICrawlerLaufService crawlerService;

    private Quelle              gewaehlteQuelle;

    private Quelle              neueQuelle;

    private Quelle              gewaehlteQuelleKopie;

    @PostConstruct
    public void init()
    {
        this.setzeDefaultQuelle();
    }

    /**
     * Setze die Default Quelle auf das erste Element
     */
    private void setzeDefaultQuelle()
    {
        if (this.getQuellen() != null && !this.getQuellen().isEmpty())
        {
            this.setGewaehlteQuelle(this.getQuellen().get(0));
        }
    }

    /**
     * Manuellen Crawlvorgang starten
     */
    public void crawleManuell()
    {
        this.crawlerService.crawl();
    }

    /**
     * Get von allen Quellen
     *
     * @return Liste von Quellen
     */
    public List<Quelle> getQuellen()
    {
        List<Quelle> quellen = this.quellenManager.getQuellen();
        return quellen;
    }

    /**
     * Get auf die aktuell gew�hlte Quelle
     *
     * @return
     */
    public Quelle getGewaehlteQuelle()
    {
        return this.gewaehlteQuelle;
    }

    /**
     * Setze die neue gew�hlte Quelle, erzeugt dabei eine Kopie
     *
     * @param geweahlteQuelle
     */
    public void setGewaehlteQuelle(Quelle geweahlteQuelle)
    {
        if (geweahlteQuelle != null)
        {
            this.gewaehlteQuelleKopie = new Quelle(geweahlteQuelle.getName(), geweahlteQuelle.getBild(), geweahlteQuelle.getRsslink(),
                                                   geweahlteQuelle.getTagOderId(), geweahlteQuelle.getFilterprofile());
        }
        this.gewaehlteQuelle = geweahlteQuelle;
    }

    /**
     * Erstelle neue Quelle
     */
    public void starteQuellenErstellen()
    {
        this.neueQuelle = new Quelle();
    }

    /**
     * Verwirft die �nderungen und setzt die Felder wieder auf die urspr�nglichen Werte.
     */
    public void verwerfeAenderungen()
    {
        this.gewaehlteQuelle.setName(this.gewaehlteQuelleKopie.getName());
        this.gewaehlteQuelle.setRsslink(this.gewaehlteQuelleKopie.getRsslink());
        this.gewaehlteQuelle.setTagOderId(this.gewaehlteQuelleKopie.getTagOderId());
    }

    /**
     * Speichert die �nderungen
     */
    public void speichereAenderung()
    {
        this.quellenManager.modifiziereQuelle(this.gewaehlteQuelle);
        this.setGewaehlteQuelle(this.getGewaehlteQuelle());
    }

    /**
     * �bernimmt die neue Quelle
     */
    public void uebernehmeNeueQuelle()
    {
        this.quellenManager.erstelleQuelle(this.neueQuelle);
        this.neueQuelle = new Quelle();
        this.setzeDefaultQuelle();
    }

    /**
     * L�scht die gew�hlte Quelle und setzt eine neue Default Quelle
     */
    public void loescheQuelle()
    {
        this.quellenManager.loescheQuelle(this.gewaehlteQuelle);
        // TODO null geht nicht!!!
        this.setGewaehlteQuelle(null);

        this.setzeDefaultQuelle();
    }

    /**
     * Boolean, ob der L�schen Button angezeigt werden muss
     *
     * @return
     */
    public boolean zeigeLoeschButton()
    {
        return true;
    }

    /**
     * Boolean, ob der Verwerfen Button angezeigt werden muss
     *
     * @return
     */
    public boolean zeigeVerwerfenButton()
    {
        return this.wurdeQuelleVeraendert();
    }

    /**
     * Boolean, ob der neue Quelle verwerfen Button angezeigt werden muss. Liefert true wenn ein Feld bef�llt ist.
     *
     * @return
     */
    public boolean zeigeNeueQuelleVerwerfenButton()
    {
        if (this.neueQuelle == null)
        {
            return false;
        }

        if (this.neueQuelle.getName() != null && !this.neueQuelle.getName().isEmpty())
        {
            return true;
        }

        if (this.neueQuelle.getRsslink() != null && !this.neueQuelle.getRsslink().isEmpty())
        {
            return true;
        }

        return false;
    }

    /**
     * Boolean, ob der Quelle erstellen Button angezeigt werden muss, liefert true wenn alle Felder bef�llt sind.
     *
     * @return
     */
    public boolean zeigeErstellenButton()
    {
        if (this.neueQuelle == null)
        {
            return false;
        }

        if (this.neueQuelle.getName() == null || this.neueQuelle.getName().isEmpty())
        {
            return false;
        }

        if (this.neueQuelle.getRsslink() == null || this.neueQuelle.getRsslink().isEmpty())
        {
            return false;
        }

        return true;
    }

    public boolean zeigeQuelleAn()
    {
        return this.getGewaehlteQuelle() != null;
    }

    /**
     * Boolean, ob der Speichern button angezeit werden muss.
     *
     * @return
     */
    public boolean zeigeSpeichernButton()
    {
        return this.wurdeQuelleVeraendert();
    }

    /**
     * Boolean, ob die Quelle ver�ndert wurde. �berpr�ft, ob Inhalte der gew�hlten Quelle und der Kopie gleich sind.
     *
     * @return
     */
    private boolean wurdeQuelleVeraendert()
    {

        if (this.gewaehlteQuelleKopie == null || this.gewaehlteQuelle == null)
        {
            return false;
        }

        if (!this.istStringGleich(this.gewaehlteQuelle.getName(), this.gewaehlteQuelleKopie.getName()))
        {
            return true;
        }
        if (!this.istStringGleich(this.gewaehlteQuelle.getRsslink(), this.gewaehlteQuelleKopie.getRsslink()))
        {
            return true;
        }
        if (!this.istStringGleich(this.gewaehlteQuelle.getTagOderId(), this.gewaehlteQuelleKopie.getTagOderId()))
        {
            return true;
        }

        return false;
    }

    /**
     * �berpr�ft ob zwei Strings gleich sind.
     *
     * @param string1
     * @param string2
     * @return
     */
    private boolean istStringGleich(String string1, String string2)
    {
        if (string1 == null && string2 == null)
        {
            return true;
        }

        if (string1 == null && string2.isEmpty())
        {
            return true;
        }

        if (string1.isEmpty() && string2 == null)
        {
            return true;
        }

        if (string1 == null || string2 == null)
        {
            return false;
        }

        if (string1.equals(string2))
        {
            return true;
        }

        return false;
    }

    /**
     * Get f�r die neue Quelle
     *
     * @return
     */
    public Quelle getNeueQuelle()
    {
        return this.neueQuelle;
    }

    /**
     * Set f�r die neue Quelle
     *
     * @param neueQuelle
     */
    public void setNeueQuelle(Quelle neueQuelle)
    {
        this.neueQuelle = neueQuelle;
    }
}
