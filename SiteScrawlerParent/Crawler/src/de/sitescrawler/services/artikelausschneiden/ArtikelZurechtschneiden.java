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

    public List<String> getVolltextartikel(String url) throws UnparsbarException
    {

        List<String> absaetze = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect(url).get();
            Map<Element, Integer> kindAnzahlP = new HashMap<>();
            // Alle P-Tags des Dokuments lesen
            Elements allePTags = doc.select("p");
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
