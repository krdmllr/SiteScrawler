package de.sitescrawler.applikation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Archiveintrag;
import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterGruppe;
import de.sitescrawler.model.FilterProfil;
import de.sitescrawler.model.Firma;
import de.sitescrawler.model.FirmenFilterGruppe;
import de.sitescrawler.model.Mitarbeiter;
import de.sitescrawler.utility.DateUtils;

@SessionScoped
@Named("data")
public class DataBean implements Serializable
{

    private static final long  serialVersionUID = 1L;

    private Nutzer             nutzer;

    private FilterGruppe       filtergruppe;

    private FilterGruppe       nutzerFiltergruppe;

    private List<FilterProfil> filterprofile    = new ArrayList<>();

    private List<Firma>        firmen           = new ArrayList<>();

    public DataBean()
    {

        this.nutzer = new Nutzer();
        this.nutzer.setNachname("Dieter");
        this.nutzer.setVorname("Hans");

        for (int i = 0; i < 15; i++)
        {
            FilterProfil fp1 = new FilterProfil();
            for (int j = 0; j < 10; j++)
            {
                fp1.getTags().add("Tag" + j);
            }
            fp1.setTitel("Filterprofil" + i);
            this.filterprofile.add(fp1);
        }

        this.nutzerFiltergruppe = new FilterGruppe();

        this.nutzerFiltergruppe.setTitel("Meine Filtergruppe");
        this.nutzerFiltergruppe.setArchiveintraege(this.getDummyArchiveintraege("Meine Filtergruppe"));
        for (int i = 0; i < 10; i++)
        {
            if (i % 2 == 0)
            {
                this.nutzerFiltergruppe.getFilterprofile().add(this.filterprofile.get(i));
            }
        }
        this.setFiltergruppe(this.nutzerFiltergruppe);

        Firma firmaVonNutzer = new Firma();
        firmaVonNutzer.setName("Eine Firma");
        Mitarbeiter nutzerArbeiter1 = new Mitarbeiter();
        nutzerArbeiter1.setIsAdmin(true);
        nutzerArbeiter1.setNutzeraccount(this.nutzer);
        firmaVonNutzer.getMitarbeiter().add(nutzerArbeiter1);

        Firma fremdFirma = new Firma();
        fremdFirma.setName("Andere Firma");
        Mitarbeiter nutzerArbeiter2 = new Mitarbeiter();
        nutzerArbeiter2.setNutzeraccount(this.nutzer);
        fremdFirma.getMitarbeiter().add(nutzerArbeiter2);

        // Testmitarbeiter
        for (int i = 0; i < 20; i++)
        {
            Nutzer testNutzer = new Nutzer();
            testNutzer.setVorname("Vorname" + i);
            testNutzer.setNachname("nachname" + i);
            Mitarbeiter mitarbeiter = new Mitarbeiter();
            mitarbeiter.setNutzeraccount(testNutzer);

            firmaVonNutzer.getMitarbeiter().add(mitarbeiter);

            Nutzer testNutzer2 = new Nutzer();
            testNutzer2.setVorname("Vorname" + i);
            testNutzer2.setNachname("nachname" + i);
            Mitarbeiter mitarbeiter2 = new Mitarbeiter();
            mitarbeiter2.setNutzeraccount(testNutzer2);
            fremdFirma.getMitarbeiter().add(mitarbeiter2);
        }
        fremdFirma.getMitarbeiter().get(3).setIsAdmin(true);

        // Testprofile
        this.firmenDummyDaten(fremdFirma);
        this.firmenDummyDaten(firmaVonNutzer);

        this.firmen.add(firmaVonNutzer);
        this.firmen.add(fremdFirma);

    }

    private void firmenDummyDaten(Firma firma)
    {
        for (int i = 0; i < 10; i++)
        {
            FilterProfil testProfil = new FilterProfil();
            testProfil.setTitel("FirmenFilterprofil" + i);
            for (int j = 0; j < 5; j++)
            {
                testProfil.getTags().add("Tag" + j);
            }
            firma.getFilterprofile().add(testProfil);
        }

        for (int j = 0; j < 2; j++)
        {
            FirmenFilterGruppe testGruppe = new FirmenFilterGruppe();
            testGruppe.setTitel("FirmenFilterGruppe" + j);
            testGruppe.setFirma(firma);
            for (int k = 0; k < 10; k++)
            {
                testGruppe.getEmpfaenger().add(firma.getMitarbeiter().get(k));
            }

            for (int k = 0; k < 2; k++)
            {
                testGruppe.getFilterprofile().add(firma.getFilterprofile().get(k));
            }

            testGruppe.setArchiveintraege(this.getDummyArchiveintraege(testGruppe.getTitel() + "Eintrag"));

            firma.getFiltergruppen().add(testGruppe);
        }
    }

    private List<Archiveintrag> getDummyArchiveintraege(String name)
    {
        List<Archiveintrag> list = new ArrayList();

        try
        {

            for (int i = 0; i < 20; i++)
            {
                Archiveintrag eintrag = new Archiveintrag();
                List<Artikel> artikelListe = new ArrayList<>();

                eintrag.setErstellungsDatum(DateUtils.asDate(LocalDateTime.now().minusDays(i)));
                for (int j = 0; j < 30; j++)
                {
                    Artikel artikel = new Artikel();
                    artikel.setAutor(name + "Autor" + i + "|" + j);
                    artikel.setBeschreibung(name + "Beschreibung" + i + "|" + j);
                    artikel.setTitel(name + "Titel" + i + "|" + j);
                    artikel.setErstellungsdatum(DateUtils.asDate(DateUtils.asLocalDateTime(eintrag.getErstellungsDatum()).plusMinutes(i)));
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
        finally
        {
            return list;
        }
    }

    public Nutzer getNutzer()
    {
        return this.nutzer;
    }

    public void setNutzer(Nutzer nutzer)
    {
        this.nutzer = nutzer;
    }

    public List<Firma> getFirmen()
    {
        return this.firmen;
    }

    public void setFirmen(List<Firma> firmen)
    {
        this.firmen = firmen;
    }

    public List<FilterProfil> getFilterprofile()
    {
        return this.filterprofile;
    }

    private void setFilterprofile(List<FilterProfil> filterprofile)
    {
        this.filterprofile = filterprofile;
    }

    public FilterGruppe getFiltergruppe()
    {
        return this.filtergruppe;
    }

    public void setFiltergruppe(FilterGruppe filtergruppe)
    {
        this.filtergruppe = filtergruppe;
    }

    public List<FilterGruppe> getFiltergruppen()
    {
        if (this.nutzer == null)
        {
            return null;
        }

        List<FilterGruppe> alleFiltergruppen = new ArrayList<>();
        alleFiltergruppen.add(this.nutzerFiltergruppe);

        for (Firma f : this.firmen)
        {
            for (FirmenFilterGruppe ffg : f.getFiltergruppen())
            {
                for (Mitarbeiter ma : ffg.getEmpfaenger())
                {
                    if (ma.getNutzeraccount() == null)
                    {
                        System.out.println(ma);
                    }
                    if (ma.getNutzeraccount().equals(this.nutzer))
                    {
                        alleFiltergruppen.add(ffg);
                    }
                }
            }
        }

        return alleFiltergruppen;
    }
}
