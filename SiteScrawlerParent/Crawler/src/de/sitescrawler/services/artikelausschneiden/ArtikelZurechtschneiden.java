package de.sitescrawler.services.artikelausschneiden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.sitescrawler.jpa.Quelle;

/**
 * @author tobias, Yvette
 * Schneidet die Artikel aus den verschiedenen Quellseite so zu,
 * dass es an Solr weitergegeben werden kann.
 *
 */
public class ArtikelZurechtschneiden
{
    // Globalen Logger holen
    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    /**
     * Gibt die Absätze eines Artikels zurück
     *
     * @param url
     *            - URL des Artikels
     * @param classOderId
     *            - Unter dem Element mit dem Klassennamen bzw. der Id wird nach dem Artikel gesucht
     * @return Absätze als Liste von Strings
     * @throws UnparsbarException
     *            - falls der Artikel nicht geparst werden konnte
     */
    public List<String> getAbsaetze(String url, Quelle quelle) throws UnparsbarException
    {
        List<String> absaetze = new ArrayList<>();

        try
        {
            Document doc = Jsoup.connect(url).get();
            Map<Element, Integer> kindAnzahlP = new HashMap<>();
            Elements allePTags = new Elements();
            String klasseOderId = quelle.getTagOderId();

            // Schaue, ob der Nutzer eine Id oder eine Klasse angegeben hat, nach dem im Artikel gesucht werden soll.
            boolean klasseOderIdVorgegeben = !(klasseOderId == null || "".equals(klasseOderId));

            // Suche die angegebene Klasse oder Id und verwende die Kinder p-Tags.
            // p-Tags stehen für Abschnitte, die mit Text gefüllt sind.
            if(klasseOderIdVorgegeben)
            {
                // Versuche p-Tags der Id hinzuzufügen
                Element fallsIdAngegeben = doc.getElementById(klasseOderId);
                if (fallsIdAngegeben != null)
                {
                    Elements elementsAbhaengigVonID = fallsIdAngegeben.select("p,h1, h2, h3, h4, h5, h6");
                    allePTags.addAll(elementsAbhaengigVonID);
                }

                // Versuche p-Tags der Klasse hinzuzufügen
                Elements elementsAbhaengigVonKlasse = doc.getElementsByClass(klasseOderId);
                for (Element element : elementsAbhaengigVonKlasse)
                {
                    allePTags.addAll(element.select("p,h1, h2, h3, h4, h5, h6"));
                }
            }

            // Falls nicht nach Id oder Klasse gesucht wurde oder keine p-Tags gefunden wurden
            // durchsuche alle p-Tags
            if (allePTags.isEmpty())
            {
                Elements select = doc.select("p");
                allePTags.addAll(select);
            }

            // Seite hat unerwarteter Weise keine p-Tags
            if (allePTags.isEmpty())
            {
                throw new UnparsbarException();
            }

            // p-Tags ihren Eltern zuordnen
            for (Element ptag : allePTags)
            {
                Element elter = ptag.parent();
                if (kindAnzahlP.containsKey(elter))
                {
                    kindAnzahlP.put(elter, (kindAnzahlP.get(elter) + 1));
                }
                else
                {
                    kindAnzahlP.put(elter, 1);
                }
            }

            // Knoten mit meisten p-Tags als Kind finden
            Entry<Element, Integer> maxEntry = null;
            for (Entry<Element, Integer> eintrag : kindAnzahlP.entrySet())
            {
                if (maxEntry == null || maxEntry.getValue() < eintrag.getValue())
                {
                    maxEntry = eintrag;
                }
            }

            // Alle p-Tags und h-Tags des gefundenen Eltern-Knotens nehmen und dessen Inhalt als Liste zurückgeben
            Elements pTagsAusArtikel = maxEntry.getKey().select("p,h1, h2, h3, h4, h5, h6");
            
            for (Element absatzPTag : pTagsAusArtikel)
            {
                absaetze.add(absatzPTag.text());

            }

            ArtikelZurechtschneiden.LOGGER.info("Absätze erfolgreich gesammelt.");

        }
        catch (IOException e)
        {
            ArtikelZurechtschneiden.LOGGER.log(Level.SEVERE,"Fehler beim Sammeln der Absätze!");
            e.printStackTrace();
        }

        return absaetze;
    }
}
