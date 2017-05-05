package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Firmenrolle;
import de.sitescrawler.model.ProjectConfig;
import de.sitescrawler.utility.DateUtils;

@SessionScoped
@Named("data")
public class DataBean implements Serializable
{

    private static final long  serialVersionUID = 1L;

    private Nutzer             nutzer;

    @Inject
    private ProjectConfig      config;

    private Filterprofilgruppe filtergruppe;

    public DataBean()
    {

        this.nutzer = new Nutzer();
        this.nutzer.setNachname("Dieter");
        this.nutzer.setVorname("Hans");

        for (int i = 0; i < 15; i++)
        {
            Filterprofil fp1 = new Filterprofil();
            fp1.setTagstring("merkel");

            fp1.setFilterprofilname("Filterprofil" + i);
            this.nutzer.getFilterprofile().add(fp1);
        }

        Filterprofilgruppe nutzerFiltergruppe = new Filterprofilgruppe();
        nutzerFiltergruppe.setTitel("Meine Filtergruppe");

        nutzerFiltergruppe.setArchiveintraege(this.getDummyArchiveintraege(""));
        for (int i = 0; i < 10; i++)
        {
            if (i % 2 == 0)
            {
                nutzerFiltergruppe.getFilterprofile().add((Filterprofil) this.nutzer.getFilterprofile().toArray()[i]);
            }
        }
        this.setFiltergruppe(nutzerFiltergruppe);

        Firma firmaVonNutzer = new Firma();
        firmaVonNutzer.setName("Eine Firma");
        Mitarbeiter nutzerArbeiter1 = new Mitarbeiter();
        nutzerArbeiter1.setFirmenrolle(Firmenrolle.Administrator);
        nutzerArbeiter1.setNutzer(this.nutzer);
        firmaVonNutzer.getMitarbeiter().add(nutzerArbeiter1);

        Firma fremdFirma = new Firma();
        fremdFirma.setName("Andere Firma");
        Mitarbeiter nutzerArbeiter2 = new Mitarbeiter();
        nutzerArbeiter2.setNutzer(this.nutzer);
        fremdFirma.getMitarbeiter().add(nutzerArbeiter2);

        // Testmitarbeiter
        for (int i = 0; i < 20; i++)
        {
            Nutzer testNutzer = new Nutzer();
            testNutzer.setVorname("Vorname" + i);
            testNutzer.setNachname("nachname" + i);
            Mitarbeiter mitarbeiter = new Mitarbeiter();
            mitarbeiter.setNutzer(testNutzer);

            firmaVonNutzer.getMitarbeiter().add(mitarbeiter);

            Nutzer testNutzer2 = new Nutzer();
            testNutzer2.setVorname("Vorname" + i);
            testNutzer2.setNachname("nachname" + i);
            Mitarbeiter mitarbeiter2 = new Mitarbeiter();
            mitarbeiter2.setNutzer(testNutzer2);
            fremdFirma.getMitarbeiter().add(mitarbeiter2);
        }
        ((Mitarbeiter) fremdFirma.getMitarbeiter().toArray()[2]).setFirmenrolle(de.sitescrawler.model.Firmenrolle.Administrator);

        // Testprofile
        this.firmenDummyDaten(fremdFirma);
        this.firmenDummyDaten(firmaVonNutzer);

        this.nutzer.getFirmen().add(firmaVonNutzer);
        this.nutzer.getFirmen().add(fremdFirma);

    }

    @PostConstruct
    void init()
    {
    }

    private void firmenDummyDaten(Firma firma)
    {
        for (int i = 0; i < 10; i++)
        {
            Filterprofil testProfil = new Filterprofil();
            testProfil.setFilterprofilname("FirmenFilterprofil" + i);

            for (int j = 0; j < 5; j++)
            {
                testProfil.setTagstring("Merkel");
            }
            firma.getFilterprofile().add(testProfil);
        }

        for (int j = 0; j < 2; j++)
        {
            Filterprofilgruppe testGruppe = new Filterprofilgruppe();
            testGruppe.setTitel("FirmenFilterGruppe" + j);
            testGruppe.setFiltermanager(firma);
            for (int k = 0; k < 10; k++)
            {
                testGruppe.getEmpfaenger().add(((Mitarbeiter) firma.getMitarbeiter().toArray()[k]).getNutzer());
            }

            for (int k = 0; k < 2; k++)
            {
                testGruppe.getFilterprofile().add((Filterprofil) firma.getFilterprofile().toArray()[k]);
            }

            testGruppe.setArchiveintraege(this.getDummyArchiveintraege(testGruppe.getTitel() + "Eintrag"));

            firma.getFilterprofilgruppen().add(testGruppe);
        }
    }

    private Set<Archiveintrag> getDummyArchiveintraege(String name)
    {

        Set<Archiveintrag> list = new HashSet<>();

        try
        {

            for (int i = 0; i < 20; i++)
            {
                Archiveintrag eintrag = new Archiveintrag();
                Set<Artikel> artikelListe = new HashSet<>();

                eintrag.setErstellungsdatum(DateUtils.asDate(LocalDateTime.now().minusDays(i)));
                for (int j = 0; j < 30; j++)
                {
                    Artikel artikel = new Artikel();
                    artikel.setAutor(name + "Autor" + i + "|" + j);
                    artikel.setBeschreibung(name + "Beschreibung" + i + "|" + j);
                    artikel.setTitel(name + "Titel" + i + "|" + j);
                    artikel.setErstellungsdatum(DateUtils.asDate(DateUtils.asLocalDateTime(eintrag.getErstellungsdatum()).plusMinutes(i)));
                    artikelListe.add(artikel);
                }
                eintrag.setArtikel(artikelListe);
                list.add(eintrag);
            }

        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
         
        return list;
         
    }

    public Nutzer getNutzer()
    {
        return this.nutzer;
    }

    public void setNutzer(Nutzer nutzer)
    {
        this.nutzer = nutzer;
    }

    public Filterprofilgruppe getFiltergruppe()
    {
        return this.filtergruppe;
    }

    public void setFiltergruppe(Filterprofilgruppe filtergruppe)
    {
        this.filtergruppe = filtergruppe;
    }

    public List<Filterprofilgruppe> getFiltergruppen()
    {
        if (this.nutzer == null)
        {
            return null;
        }

        List<Filterprofilgruppe> alleFiltergruppen = new ArrayList<>();
        alleFiltergruppen.addAll(this.nutzer.getFilterprofilgruppen());

        for (Firma f : this.nutzer.getFirmen())
        {
            for (Filterprofilgruppe ffg : f.getFilterprofilgruppen())
            {
                for (Nutzer ma : ffg.getEmpfaenger())
                {
                    if (ma.equals(this.nutzer))
                    {
                        alleFiltergruppen.add(ffg);
                    }
                }
            }
        }

        return alleFiltergruppen;
    }

    public ProjectConfig getConfig()
    {
        return this.config;
    }
}
