package de.sitescrawler.artikelausschneiden;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.sitescrawler.model.VolltextArtikel;

class ZeitArtikelZurechtschneiden
{

    /**
     * Ließt den Artikel einer Zeit Seite aus.
     *
     * @param url
     *            URL des Artikels
     * @return Ausgeschnittener Artikel
     * @throws IOException
     *             bei I/O Error
     * @throws UnparsbarException
     *             TODO
     */
    public VolltextArtikel getZeitArtikel(String url) throws IOException, UnparsbarException
    {
        VolltextArtikel artikel = new VolltextArtikel();
        Document doc = Jsoup.connect(url).get();
        // // Extrahiere überschrift
        // Elements ueberschrift = doc.getElementsByClass("article-heading__title");
        // artikel.setUeberschrift(ueberschrift.get(0).text());
        //
        // // Extrahiere Zusammenfassung
        // Elements zusammenfassung = doc.getElementsByClass("summary");
        // artikel.setZusammenfassung(zusammenfassung.get(0).text());

        // Dieser Block dient dazu alle bekannten Artikelarten mit anderer Struktur abzufangen (können erweitert werden)
        // Bezahlartikel können nicht geparst werden
        Element bezahlArtikelHinweis = doc.getElementById("paywall-register");
        if (bezahlArtikelHinweis != null)
        {
            throw new UnparsbarException();
        }

        // Freitextartikel haben ein Format, welches von jedem Standard abweicht
        if (url.contains("zeit.de/freitext/"))
        {
            throw new UnparsbarException();

        }

        // Blogartikel haben ein Format, welches vom Standard abweicht
        if (url.contains("blog.zeit.de"))
        {
            throw new UnparsbarException();
        }

        try
        {
            // Einzelnen Absätze lesen und in Map von Artikel schreiben
            Elements artikelabschnitt = doc.getElementsByClass("article-page");
            Elements artikelabsaetze = artikelabschnitt.get(0).getElementsByTag("p");
            for (Element artikelabsatz : artikelabsaetze)
            {
                artikel.getArtikelAbsaetze().add(artikelabsatz.text());

            }

            // Tags auslesen Tagreihenfolge UL mit Klasse article-tags__list -> LI -> A -> Inhalt auslesen
            Elements tags = doc.getElementsByClass("article-tags__list");
            if (!tags.isEmpty())
            {
                tags = tags.get(0).getElementsByTag("li");

                for (Element tag : tags)
                {
                    tag = tag.getElementsByTag("a").get(0);
                    artikel.getTags().add(tag.text());
                }
            }
        }
        catch (Exception e)
        {
            // So soll sichergestellt werden, dass wenn ein Artikel unerwartet vom Schema abweicht, auch als unparsbar
            // eingestuft wird
            throw new UnparsbarException();
        }
        return artikel;

    }

}
