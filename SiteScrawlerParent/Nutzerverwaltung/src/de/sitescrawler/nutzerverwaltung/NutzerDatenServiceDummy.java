package de.sitescrawler.nutzerverwaltung;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Firma;
import de.sitescrawler.jpa.Mitarbeiter;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.Firmenrolle;
import de.sitescrawler.nutzerverwaltung.interfaces.INutzerDatenService;
import de.sitescrawler.utility.DateUtils;

/**
 * Dummy implementierung für den INutzerDatenService.
 *
 * @author klmue
 *
 */
@SessionScoped
public class NutzerDatenServiceDummy implements Serializable, INutzerDatenService
{

    private static final long serialVersionUID = 1L;
    private Nutzer            nutzer;

    @Override
    public Nutzer getNutzer()
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
        nutzerFiltergruppe.setNutzer(this.nutzer);
        nutzerFiltergruppe.setTitel("Meine Filtergruppe");
        this.nutzer.getFilterprofilgruppen().add(nutzerFiltergruppe);

        nutzerFiltergruppe.setArchiveintraege(this.getDummyArchiveintraege(""));
        for (int i = 0; i < 10; i++)
        {
            if (i % 2 == 0)
            {
                nutzerFiltergruppe.getFilterprofile().add((Filterprofil) this.nutzer.getFilterprofile().toArray()[i]);
            }
        }
        // this.setFiltergruppe(nutzerFiltergruppe);

        Firma firmaVonNutzer = new Firma();
        firmaVonNutzer.setName("Eine Firma");
        Mitarbeiter nutzerArbeiter1 = new Mitarbeiter();
        nutzerArbeiter1.setFirmenrolle(Firmenrolle.Administrator);
        nutzerArbeiter1.setNutzer(this.nutzer);
        nutzerArbeiter1.setFirma(firmaVonNutzer);
        this.nutzer.getMitarbeiter().add(nutzerArbeiter1);
        nutzerArbeiter1.setFirma(firmaVonNutzer);
        firmaVonNutzer.getMitarbeiter().add(nutzerArbeiter1);

        Firma fremdFirma = new Firma();
        fremdFirma.setName("Andere Firma");
        Mitarbeiter nutzerArbeiter2 = new Mitarbeiter();
        nutzerArbeiter2.setNutzer(this.nutzer);
        this.nutzer.getMitarbeiter().add(nutzerArbeiter2);
        nutzerArbeiter2.setFirma(fremdFirma);
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

        return this.nutzer;
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
            testGruppe.setFirma(firma);
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
                    artikel.setBeschreibung(name + "Beschreibung" + i + "|" + j + "    "
                                            + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                    artikel.setTitel(name + "Titel" + i + "|" + j);
                    for (int k = 0; k < 4; k++)
                    {
                        artikel.getAbsaetzeArtikel().add(
                                        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
                    }
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

    @Override
    public void setNutzer(String uid)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void aendereEmailAdresse(String neueEmailAdresse, String passwort)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void aenderePasswort(String neuesPasswort, String altesPasswort)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void loescheNutzer(String passwort)
    {
        // TODO Auto-generated method stub

    }

}
