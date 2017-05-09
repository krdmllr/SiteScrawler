package de.sitescrawler.solr;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.solr.interfaces.ISolrService;

@ApplicationScoped
@Named
public class SolrService implements ISolrService, Serializable
{
    private static final long             serialVersionUID = 1L;

    private SolrClient                    solrClient;

    // private static final String SolrUrl = "http://sitescrawler.de:8983/solr/testdaten";
    // private static final String SolrUrl = "http://sitescrawler.de:8983/solr/spielwiesewilliam1";
    private static final String           SolrUrl          = "http://sitescrawler.de:8983/solr/sitescrawler_dev_solr"; // TODO:
                                                                                                                       // in
                                                                                                                       // config-Datei
                                                                                                                       // auslagern
    private static final SimpleDateFormat formatter        = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");

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
    public void addArtikel(List<Artikel> artikel)
    {
        artikel.forEach(a -> a.setSolrdatum(SolrService.formatter.format(a.getErstellungsdatum())));
        try
        {
            this.solrClient.addBeans(artikel);
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
        SolrQuery solrQuery = new SolrQuery();
        String query = filterprofil.getTagstring();
        solrQuery.setQuery(query);
        return this.getArtikel(solrQuery);
    }

    private List<Artikel> getArtikel(SolrQuery solrQuery)
    {
        List<Artikel> artikel = new ArrayList<>();
        try
        {
            QueryResponse response = this.solrClient.query(solrQuery);
            artikel = response.getBeans(Artikel.class);
            for (Artikel a : artikel)
            {
                a.setErstellungsdatum(SolrService.formatter.parse(a.getSolrdatum()));
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
        return this.getArtikel(new SolrQuery("*:*"));
    }

    @Override
    public Artikel sucheArtikelAusID(String id)
    {
        SolrQuery solrQuery = new SolrQuery("id:" + id);
        List<Artikel> artikel = this.getArtikel(solrQuery);
        return artikel.isEmpty() ? null : artikel.get(0);
    }
}
