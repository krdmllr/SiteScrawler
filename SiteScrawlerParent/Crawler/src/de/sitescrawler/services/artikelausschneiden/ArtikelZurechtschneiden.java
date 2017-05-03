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

import de.sitescrawler.jpa.Quelle; 

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
    public List<String> getAbsaetze(String url, Quelle quelle) throws UnparsbarException
    {
        List<String> absaetze = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect(url).get();
            Map<Element, Integer> kindAnzahlP = new HashMap<>();
            Elements allePTags = new Elements();
            String classOderId = quelle.getTagOderId();
            
            //Schaue ob der Nutzer eine Id oder eine Klasse angegeben hat, nach dem der Artikel gesucht werden soll.
            boolean klasseOderIdVorgegeben = !(classOderId == null || "".equals(classOderId));
            
            //Suche die angegebene Klasse oder Id und verwende die Kinder p Tags.
            if(klasseOderIdVorgegeben) 
            {
                //Versuche P-Tags der Id hinzuzufügen
                Element fallsIdAngegeben = doc.getElementById(classOderId);
                if (fallsIdAngegeben != null)
                {
                    Elements elementsAbhaengigVonID = fallsIdAngegeben.getElementsByTag("p");
                    allePTags.addAll(elementsAbhaengigVonID);
                }

                //Versuche P-Tags der Klasse hinzuzufügen
                Elements elementsAbhaengigVonKlasse = doc.getElementsByClass(classOderId);
                for (Element element : elementsAbhaengigVonKlasse)
                {
                    allePTags.addAll(element.getElementsByTag("p"));
                }
            }
            
            //Falls nicht nach Id oder Klasse gesucht wurde oder keine P-Tags gefunden wurden durchsuche alle P-Tags
            if (allePTags.isEmpty())
            {
                Elements select = doc.select("p");
                allePTags.addAll(select);
            }
            
            //Seite hat unerwarteter Weise keine P-Tags
            if (allePTags.isEmpty())
            {
                throw new UnparsbarException();
            }
            
            //Ordne P-Tags ihren Eltern zu
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
            
            // Knoten mit meisten P-tags als Kind finden
            Entry<Element, Integer> maxEntry = null;
            for (Entry<Element, Integer> eintrag : kindAnzahlP.entrySet())
            {
                if (maxEntry == null || maxEntry.getValue() < eintrag.getValue())
                {
                    maxEntry = eintrag;
                }
            } 
            
            //Nimm alle P-Tags des gefundenen Eltern-Knotens und gebe dessen Inhalt als Liste zurück.
            Elements pTagsAusArtikel = maxEntry.getKey().getElementsByTag("p");

            for (Element absatzPTag : pTagsAusArtikel)
            {
                absaetze.add(absatzPTag.text());

            }
        }
        catch (IOException e)
        {
            // TODO Log
            e.printStackTrace();
        }
        return absaetze;
    }
}
