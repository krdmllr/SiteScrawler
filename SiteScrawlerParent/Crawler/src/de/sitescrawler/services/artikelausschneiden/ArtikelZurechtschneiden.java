package de.sitescrawler.services.artikelausschneiden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArtikelZurechtschneiden
{

    /**
     * Gibt die Absätze eines Artikels zurück
     *
     * @param url
     *            - URL des Artikels
     * @param classOderId
     *            - Unter dem Element mit dem Klassennamen bzw. der Id wird nach dem Artikel geucht
     * @return Absätze als Liste von Strings
     * @throws UnparsbarException
     *             fals der Artikel nicht geparset werden konnte
     */
    public List<String> getAbsaetze(String url, String classOderId) throws UnparsbarException
    {

        List<String> absaetze = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect(url).get();
            Elements select2 = doc.select("p");
            Map<Element, Integer> kindAnzahlP = new HashMap<>();
            Elements allePTags = new Elements();
            // Alle P-Tags des Dokuments lesen (wenn classOderId gesetzt ist abhängig davon)
            if (classOderId == null || classOderId.equals(""))
            {
                Elements select = doc.select("p");

                allePTags.addAll(select);
            }
            else
            {
                // Füge PTags der Id hinzu
                Element fallsIdAngegeben = doc.getElementById(classOderId);
                if (fallsIdAngegeben != null)
                {
                    Elements elementsAbhängigVonID = fallsIdAngegeben.getElementsByTag("p");
                    allePTags.addAll(elementsAbhängigVonID);
                }

                // Füge PTags der Klassen hinzu
                Elements elementsAbhängigVonKlasse = doc.getElementsByClass(classOderId);
                for (Element element : elementsAbhängigVonKlasse)
                {
                    allePTags.addAll(element.getElementsByTag("p"));
                }
                // Wenn Seitenstruktur sich innerhalb eines Anbieter unterscheidet -> Bestmögliches Parsen
                if (allePTags.isEmpty())
                {
                    Elements select = doc.select("p");
                    allePTags.addAll(select);
                }
            }
            // Seite hat unerwarteter Weise keine P-Tags
            if (allePTags.size() == 0)
            {
                throw new UnparsbarException();
            }

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
            // Konoten mit meisten P-tags als Kind finden
            Entry<Element, Integer> maxEntry = null;
            for (Entry<Element, Integer> eintrag : kindAnzahlP.entrySet())
            {
                if (maxEntry == null || maxEntry.getValue() < eintrag.getValue())
                {
                    maxEntry = eintrag;
                }
            }
            Elements pTagsAusArtikel = maxEntry.getKey().getElementsByTag("p");

            for (Element absatzPTag : pTagsAusArtikel)
            {
                absaetze.add(absatzPTag.text());
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return absaetze;
    }

}
