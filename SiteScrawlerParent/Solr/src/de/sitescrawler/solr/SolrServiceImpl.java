package de.sitescrawler.solr;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import de.sitescrawler.model.Artikel;
import de.sitescrawler.model.FilterProfil;

public class SolrServiceImpl {

	private SolrClient solrClient;
	private static final String SolrUrl = "http://sitescrawler.de:8983/solr/gettingstarted";

	public SolrServiceImpl() {
		this.solrClient = new HttpSolrClient.Builder(SolrUrl).build();
	}

	public void addArtikel(Artikel artikel) {
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		solrInputDocument.addField("autor", artikel.getAutor());
		solrInputDocument.addField("titel", artikel.getTitel());
		solrInputDocument.addField("beschreibung", artikel.getBeschreibung());
		solrInputDocument.addField("link", artikel.getLink());
		solrInputDocument.addField("erstellungsdatum", artikel.getErstellungsdatum());
		try {
			this.solrClient.add(solrInputDocument);
			this.solrClient.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Artikel> sucheArtikel(FilterProfil filterProfil){
		List<Artikel> artikel = new ArrayList<>();
		SolrQuery solrQuery = new SolrQuery();
		String query = filterProfil.getTags().stream().reduce((s1,s2) -> s1.concat(" " + s2)).get();
		solrQuery.setQuery(query);
		try {
			QueryResponse response = this.solrClient.query(solrQuery);
			SolrDocumentList results = response.getResults();
			
			Object object = results.get(0).get("autor");
			Object object2 = results.get(0).get("erstellungsdatum");
			results.forEach(e->artikel.add(new Artikel( (Date)e.get("erstellungsdatum"),(String) e.get("autor"), (String)e.get("titel"), (String)e.get("beschreibung"),(String) e.get("link"))));
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return artikel;
	}
	
	public void clearSolr(){
		try {
			this.solrClient.deleteByQuery("*:*");
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
}
