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

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterProfil;

public class SolrServiceImpl
{

    private SolrClient                    solrClient;
    private static final String           SolrUrl   = "http://sitescrawler.de:8983/solr/gettingstarted";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");

    public SolrServiceImpl()
    {
        this.solrClient = new HttpSolrClient.Builder(SolrServiceImpl.SolrUrl).build();
    }

    public void addArtikel(Artikel artikel)
    {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("autor", artikel.getAutor());
        solrInputDocument.addField("titel", artikel.getTitel());
        solrInputDocument.addField("beschreibung", artikel.getBeschreibung());
        solrInputDocument.addField("link", artikel.getLink());
        solrInputDocument.addField("erstellungsdatum", SolrServiceImpl.formatter.format(artikel.getErstellungsdatum()));
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

    public List<Artikel> sucheArtikel(FilterProfil filterProfil)
    {
        List<Artikel> artikel = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery();
        String query = filterProfil.getTags().stream().map(s -> "\"" + s + "\"").reduce((s1, s2) -> s1.concat(" " + s2)).get();
        query = query.replace(":", "");
        solrQuery.setQuery(query);
        try
        {
            QueryResponse response = this.solrClient.query(solrQuery);
            SolrDocumentList results = response.getResults();
            for (SolrDocument solrDocument : results)
            {
                String date = ((ArrayList<String>) solrDocument.get("erstellungsdatum")).get(0);
                Date erstellungsdatum = SolrServiceImpl.formatter.parse(date);
                String autor = ((ArrayList<String>) solrDocument.get("autor")).get(0);
                String titel = ((ArrayList<String>) solrDocument.get("titel")).get(0);
                String beschreibung = ((ArrayList<String>) solrDocument.get("beschreibung")).get(0);
                String link = ((ArrayList<String>) solrDocument.get("link")).get(0);
                artikel.add(new Artikel(erstellungsdatum, autor, titel, beschreibung, link));
            }
        }
        catch (SolrServerException | IOException | ParseException e)
        {
            e.printStackTrace();
        }
        return artikel;
    }

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
}
