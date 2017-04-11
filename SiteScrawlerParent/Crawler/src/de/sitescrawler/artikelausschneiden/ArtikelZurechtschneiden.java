package de.sitescrawler.artikelausschneiden;

import java.io.IOException;

import de.sitescrawler.model.VolltextArtikel;

public class ArtikelZurechtschneiden
{

    private ZeitArtikelZurechtschneiden          zeitArtikelZurechtschneiden;
    private SpiegelOnlineArtikelZurechtschneiden spiegelOnlineArtikelZurechtschneiden;

    private static ArtikelZurechtschneiden       artikelZurechtschneiden;

    private ArtikelZurechtschneiden()
    {
        this.zeitArtikelZurechtschneiden = new ZeitArtikelZurechtschneiden();
        this.spiegelOnlineArtikelZurechtschneiden = new SpiegelOnlineArtikelZurechtschneiden();
    }

    public static ArtikelZurechtschneiden getInstance()
    {
        if (ArtikelZurechtschneiden.artikelZurechtschneiden == null)
        {
            ArtikelZurechtschneiden.artikelZurechtschneiden = new ArtikelZurechtschneiden();
        }
        return ArtikelZurechtschneiden.artikelZurechtschneiden;
    }

    /**
     * Gibt den ausgeschnittenen Artikel eines bestimmten Presseanbieters als VolltextArtikel zurück.
     *
     * @param url
     *            - Url des Artikels
     * @param presseanbieter
     *            - Presseanbieter des Artikels
     * @return VolltextArtikel des Artikels hinter der url
     * @throws IOException
     *             - bei I/O Fehlern
     * @throws UnparsbarException
     *             - Wenn ein Arikel aus bestimmten Gründen nicht parsbar ist
     */
    public VolltextArtikel getVolltextArtikel(String url, Presseanbieter presseanbieter) throws IOException, UnparsbarException
    {
        switch (presseanbieter)
        {
            case ZeitOnline:
                return this.zeitArtikelZurechtschneiden.getZeitArtikel(url);
            case SpiegelOnline:
                return this.spiegelOnlineArtikelZurechtschneiden.getSpiegelOnlineArtikel(url);

            default:
                return null;

        }

    }

}
