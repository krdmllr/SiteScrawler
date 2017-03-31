package de.sitescrawler.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import de.sitescrawler.model.Artikel;

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
	
}
