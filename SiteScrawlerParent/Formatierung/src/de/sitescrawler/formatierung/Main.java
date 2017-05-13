package de.sitescrawler.formatierung;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;

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
        Artikel twitterArtikel = new Artikel();

        artikelEins.setAutor("Autor Eins");
        artikelEins.setBeschreibung("Artikel Eins. Bla bla bla");
        artikelEins.setTitel("Der erste Artikel");
        artikelEins.setLink("https://de.wikipedia.org/wiki/Haushund");
        Date datumArtikelEins = new Date();
        artikelEins.setErstellungsdatum(datumArtikelEins);

        twitterArtikel.setAutor("dvnntsywb");
        twitterArtikel.setBeschreibung(
                        "RT @TheRegister: - #WannaCry ransomware attacks networks in 74 nations\n- MS emits emergency patches for WinXP+\n- Hero halts epidemic\nhttps:…");
        twitterArtikel.setTitel("Tweet863324831449661442");
        twitterArtikel.setLink("https://twitter.com/dvnntsywb/status/863324831449661442");
        Date datumArtikelTwitter = new Date();
        twitterArtikel.setErstellungsdatum(datumArtikelTwitter);

        Set<Artikel> setArtikel = new HashSet<>(0);
        setArtikel.add(artikelEins);
        setArtikel.add(twitterArtikel);

        eintrag.setArtikel(setArtikel);

        String ausgabePlain = fs.generierePlaintextZusammenfassung(eintrag);

        System.out.println(ausgabePlain);

        System.out.println("---------------------------------");

        String ausgabeHTML = fs.generiereHtmlZusammenfassung(eintrag);

        if (!ausgabeHTML.isEmpty())
        {
            System.out.println("HTML-Konvertierung hat funktioniert.");
        }

        System.out.println("---------------------------------");

        ByteArrayDataSource pdfAusgabe = fs.generierePdfZusammenfassung(eintrag);

        // PDF anzeigen können
        InputStream in = null;
        try
        {
            in = pdfAusgabe.getInputStream();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File targetFile = new File("targetFile.pdf");

        try
        {
            java.nio.file.Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        IOUtils.closeQuietly(in);

        System.out.println("PDF-Konvertierung fertig. Keine Aussage über Erfolg.");
    }

}
