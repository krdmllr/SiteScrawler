package de.sitescrawler.formatierung;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;

public class Main
{

    public static void main(String[] args)
    {
        FormatiererService fs = new FormatiererService();

        // Für Testzwecke (Archiveintrag mit zwei Artikeln)
        Archiveintrag eintrag = new Archiveintrag();

        Artikel artikelEins = new Artikel();
        Artikel artikelZwei = new Artikel();

        artikelEins.setAutor("Autor Eins");
        artikelEins.setBeschreibung("Artikel Eins. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        artikelEins.setTitel("Der erste Artikel");
        artikelEins.setLink("beispiellink.de/artikelEins");
        Date datumArtikelEins = new Date();
        artikelEins.setErstellungsdatum(datumArtikelEins);

        artikelZwei.setAutor("Autor Zwei");
        artikelZwei.setBeschreibung("Artikel Zwei. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        artikelZwei.setTitel("Der zweite Artikel");
        artikelZwei.setLink("beispiellink.de/artikelZwei");
        Date datumArtikelZwei = new Date();
        artikelZwei.setErstellungsdatum(datumArtikelZwei);

        Set<Artikel> setArtikel = new HashSet<>(0);
        setArtikel.add(artikelEins);
        setArtikel.add(artikelZwei);

        eintrag.setArtikel(setArtikel);

        String ausgabePlain = fs.generierePlaintextZusammenfassung(eintrag);

        System.out.println(ausgabePlain);

        System.out.println("---------------------------------");

        String ausgabeHTML = fs.generiereHtmlZusammenfassung(eintrag);

        if(!ausgabeHTML.isEmpty())
        {
            System.out.println("HTML-Konvertierung hat funktioniert.");
        }

        System.out.println("---------------------------------");
    }

}
