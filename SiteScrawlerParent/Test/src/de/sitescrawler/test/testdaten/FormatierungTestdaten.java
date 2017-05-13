package de.sitescrawler.test.testdaten;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import de.sitescrawler.formatierung.FormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;

public class FormatierungTestdaten
{
    public String        plainText     = "";
    public String        htmlString    = "";
    public Archiveintrag archiveintrag = null;

    public FormatierungTestdaten()
    {
        Date aktuellesDatum = new Date();
        FormatiererService fs = new FormatiererService();
        String aktuellesDatumString = fs.wandleDatumUm(aktuellesDatum);

        plainText = "Der erste Artikel\nAutor Eins\n" + aktuellesDatumString + "\nArtikel Eins.\nhttps://de.wikipedia.org/wiki/Haushund\n\n";
        htmlString = "<!doctype html><html><style>h1 { font-size: 36px;}h2 { font-size: 22px;}#bild { float: left; }#text { float: center; }#container { padding: 15px;}header {  color : #ddd;    background-color: #384f94; text-align: center;    padding: 10px;}footer { color : #ddd;    background-color: #384f94; text-align: right; padding: 5px;}h3 { font-size: 20px;}p { font-size: 16px;}#artikel { border: 2px solid #449d44; margin: 1px; padding: 10px;}body { font-family: Arial;}.button { background-color:#449d44; border:1px solid #449d44; color:#ddd; padding: 3px;}</style><head> <meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\"> <title>Pressespiegel SiteScrawler</title></head><body> <header> <div id=\"container\">  <!-- Bild fuer Header -->  <div id=\"bild\">        <img height=\"150\" width=\"150\" src=\"https://sitescrawler.de/javax.faces.resource/image/logo.svg.xhtml?ln=common\"/>  </div>  <!-- Text fuer Header -->  <div id=\"text\">         <h1><b>Ihr Pressespiegel von SiteScrawler</b></h1>  <h2>Viel Vergnügen mit Ihrem persönlichen Pressespiegel!</h2>  </div>  </div> </header><div id=\"artikel\"><h3><b>Der erste Artikel</b></h3><p id=\"datumUndAutor\">Von Autor Eins am "
                     + aktuellesDatumString
                     + "</p><p id=\"beschreibung\">Artikel Eins.</p><a target=\"_blank\" href=\"https://de.wikipedia.org/wiki/Haushund\" class=\"button\">Zum vollen Artikel</a></div><footer> Dieser Pressespiegel wurde von SiteScrawler erstellt und versandt.<br> <a target=\"_blank\" href=\"https://sitescrawler.de\">Klicken Sie hier</a> um zu uns zu kommen.</footer></body></html>";
        archiveintrag = new Archiveintrag();

        Artikel artikelEins = new Artikel();

        artikelEins.setAutor("Autor Eins");
        artikelEins.setBeschreibung("Artikel Eins.");
        artikelEins.setTitel("Der erste Artikel");
        artikelEins.setLink("https://de.wikipedia.org/wiki/Haushund");
        Date datumArtikelEins = new Date();
        artikelEins.setErstellungsdatum(datumArtikelEins);

        Set<Artikel> setArtikel = new HashSet<>(0);
        setArtikel.add(artikelEins);

        archiveintrag.setArtikel(setArtikel);
    }
}
