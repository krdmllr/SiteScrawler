package de.sitescrawler.services.artikelausschneiden;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.sitescrawler.model.VolltextArtikel;

public class SpiegelOnlineArtikelZurechtschneiden
{

    public VolltextArtikel getSpiegelOnlineArtikel(String url) throws UnparsbarException, IOException
    {
        VolltextArtikel artikel = new VolltextArtikel();
        Document doc = Jsoup.connect(url).get();

        try
        {
            Element artikelInhalt = doc.getElementById("js-article-column");
            Element beinhaltetArtikelPTags = artikelInhalt.getElementsByClass("article-section clearfix").get(0);
            Elements artikelPTags = beinhaltetArtikelPTags.getElementsByTag("p");

            for (Element ptag : artikelPTags)
            {
                artikel.getArtikelAbsaetze().add(ptag.text());
            }

            // Keine Tags bei Spiegel Online hinterlegt -> Keine Tags hinzufügen
        }
        catch (Exception e)
        {
            // Um Sonderfälle auslesen zu können -> gesondert vorher behandeln
            e.printStackTrace();
            // So soll sichergestellt werden, dass wenn ein Artikel unerwartet vom Schema abweicht, auch als unparsbar
            // eingestuft wird
            throw new UnparsbarException();
        }
        return artikel;

    }

}
