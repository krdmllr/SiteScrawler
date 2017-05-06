package de.sitescrawler.solr;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.solr.interfaces.ISolrService;

public class SolrService implements ISolrService
{

    private SolrClient                    solrClient;
    
    
//    private static final String           SolrUrl   = "http://sitescrawler.de:8983/solr/testdaten";
//    private static final String           SolrUrl   = "http://sitescrawler.de:8983/solr/spielwiesewilliam1";
    private static final String           SolrUrl   = "http://sitescrawler.de:8983/solr/sitescrawler_dev_solr";  //TODO: in config-Datei auslagern
//    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
    private static final SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");

    public SolrService()
    {
        this.solrClient = new HttpSolrClient.Builder(SolrService.SolrUrl).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.sitescrawler.solr.ISolrService#addArtikel(de.sitescrawler.model. Artikel)
     */
    @Override
    public void addArtikel(Artikel artikel)
    {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("autor", artikel.getAutor());
        solrInputDocument.addField("titel", artikel.getTitel());
        solrInputDocument.addField("beschreibung", artikel.getBeschreibung());
        solrInputDocument.addField("link", artikel.getLink());
        solrInputDocument.addField("erstellungsdatum", SolrService.formatter.format(artikel.getErstellungsdatum()));
        try
        {
            this.solrClient.add(solrInputDocument);
            this.solrClient.commit();
        }
        catch (SolrServerException | IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see de.sitescrawler.solr.ISolrService#sucheArtikel(de.sitescrawler.model. FilterProfil)
     */
    @Override
    public List<Artikel> sucheArtikel(List<Filterprofil> filterprofile)
    {

        List<Artikel> artikel = new ArrayList<>();

        for (Filterprofil filterprofil : filterprofile)
        {
            artikel.addAll(this.sucheArtikel(filterprofil));
        }

        return artikel;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.sitescrawler.solr.ISolrService#sucheArtikel(de.sitescrawler.model. FilterProfil)
     */

    @Override
    public List<Artikel> sucheArtikel(Filterprofil filterprofil)
    {

        List<Artikel> artikel = new ArrayList<>();

        SolrQuery solrQuery = new SolrQuery();
        String query = filterprofil.getTagstring();
        solrQuery.setQuery(query);
        try
        {
            QueryResponse response = this.solrClient.query(solrQuery);
            SolrDocumentList results = response.getResults();
            for (SolrDocument solrDocument : results)
            {
                String date = SolrService.formatter.format((solrDocument.getFirstValue("erstellungsdatum")));
                Date erstellungsdatum = SolrService.formatter.parse(date);
                String autor =  (String) solrDocument.get("autor");
                String titel = (String) solrDocument.get("titel");
                String beschreibung = (String) solrDocument.get("beschreibung");
                String link =  (String) solrDocument.get("link");
                artikel.add(new Artikel(erstellungsdatum, autor, titel, beschreibung, link,null));
            }
        }
        catch (SolrServerException | IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return artikel;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.sitescrawler.solr.ISolrService#clearSolr()
     */
    @Override
    public void clearSolr()
    {
        try
        {
            this.solrClient.deleteByQuery("*:*");
        }
        catch (SolrServerException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artikel> getAlleArtikel()
    {
        List<Artikel> artikel = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery("*:*");
        try
        {
            QueryResponse response = this.solrClient.query(solrQuery);
            SolrDocumentList results = response.getResults();
            for (SolrDocument solrDocument : results)
            {
                String date = SolrService.formatter.format((solrDocument.getFirstValue("erstellungsdatum")));
                Date erstellungsdatum = SolrService.formatter.parse(date);
                String autor =  (String) solrDocument.get("autor");
                String titel = (String) solrDocument.get("titel");
                String beschreibung = (String) solrDocument.get("beschreibung");
                String link =  (String) solrDocument.get("link");
                artikel.add(new Artikel(erstellungsdatum, autor, titel, beschreibung, link));
            }
        }
        catch (SolrServerException | IOException | ParseException e)
        {
            e.printStackTrace();
        }
        return artikel;
    }
    
    public List<Artikel> getArtikelAusID(List<String> ids){
        List<Artikel> artikel = new ArrayList<Artikel>();
        for(String id : ids){
            SolrQuery solrQuery = new SolrQuery("id:" + id);
            try
            {
                QueryResponse response = this.solrClient.query(solrQuery);
                SolrDocumentList results = response.getResults();
                for (SolrDocument solrDocument : results)
                {
                    String date = SolrService.formatter.format((solrDocument.getFirstValue("erstellungsdatum")));
                    Date erstellungsdatum = SolrService.formatter.parse(date);
                    String autor =  (String) solrDocument.get("autor");
                    String titel = (String) solrDocument.get("titel");
                    String beschreibung = (String) solrDocument.get("beschreibung");
                    String link =  (String) solrDocument.get("link");
                    artikel.add(new Artikel(erstellungsdatum, autor, titel, beschreibung, link));
                }
            }
            catch (SolrServerException | IOException | ParseException e)
            {
                e.printStackTrace();
            }

        }
            return artikel;
    }
    
}
