package de.sitescrawler.test.unittests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Assert;
import org.junit.Test;

import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filtermanager;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Quelle;
import de.sitescrawler.solr.SolrService;

public class SolrUnitTest
{

    @Test
    public void testeAddArtikel()
    {
        SolrClient solrClient = new HttpSolrClient.Builder("http://sitescrawler.de:8983/solr/spielwiesewilliam").build();
        SolrService s = new SolrService();
        List<Artikel> artikel = new ArrayList<Artikel>();
        Quelle quelle = new Quelle("Spiegel", "http://www.spiegel.de/schlagzeilen/index.rss");
        quelle.setQid(14);
        artikel.add(new Artikel(new Date(), "Test", "Unittest", "Beschreibung", "www.test.de", quelle));
        s.addArtikel(artikel);
        SolrQuery sq = new SolrQuery("autor: Test");
        try
        {
            QueryResponse response = solrClient.query(sq);;
            artikel = new ArrayList<>(new HashSet<>(response.getBeans(Artikel.class)));
        }
        catch (SolrServerException | IOException e)
        {
            e.printStackTrace();
        }

        Assert.assertNotNull(artikel);
        try
        {
            solrClient.deleteByQuery("autor: Test");
        }
        catch (SolrServerException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testeSucheArtikelFilterprofil()
    {
        Filterprofil fp = new Filterprofil(new Filtermanager(), "Test");
        fp.setTagstring("*:*");
        SolrService s = new SolrService();
        Assert.assertNotNull(s.sucheArtikel(fp));
    }

    public void testeSucheArtikelFilterprofilListe()
    {
        Filterprofil fp = new Filterprofil(new Filtermanager(), "Test");
        fp.setTagstring("*:*");
        List<Filterprofil> fpliste = new ArrayList<Filterprofil>();
        fpliste.add(fp);
        SolrService s = new SolrService();
        Assert.assertNotNull(s.sucheArtikel(fpliste, null));
    }

    @Test
    public void testeSucheArtikelMitLink()
    {
        String link = "http://www.spiegel.de/kultur/gesellschaft/spiegel-daily-so-funktioniert-die-smarte-abendzeitung-a-1146953.html#ref=rss";
        SolrService s = new SolrService();
        Assert.assertNotNull(s.sucheArtikelMitLink(link));
    }

    @Test
    public void testeGetAlleArtikel()
    {
        SolrService s = new SolrService();
        Assert.assertNotNull(s.getAlleArtikel());
    }
}
