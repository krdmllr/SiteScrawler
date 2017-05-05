package de.sitescrawler.reporter.implementations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import de.sitescrawler.email.MailSenderService;
import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.formatierer.interfaces.IFormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Intervall;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.management.FiltergruppenZugriffsManager;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.solr.SolrService;
import de.sitescrawler.solr.interfaces.ISolrService;
import de.sitescrawler.utility.DateUtils;

public class ArchiveintragErstellen implements Runnable{

	@Inject
	ISolrService solr;
	
	@Inject
	IMailSenderService mailSenderService;
	
	@Inject
	IFiltergruppenZugriffsManager filtergruppenZugriff;
	
	@Inject
	IFormatiererService formatiererService;
	
	Filterprofilgruppe filtergruppe;
	LocalDateTime aktuelleZeit;
	
	public ArchiveintragErstellen(Filterprofilgruppe filtergruppe,
			LocalDateTime aktuelleZeit){
		
		this.filtergruppe = filtergruppe;
		this.aktuelleZeit = aktuelleZeit;
		
		if(solr == null){
			solr = new SolrService();
		}
		
		if(mailSenderService == null){
			mailSenderService = new MailSenderService();
		}
		
		if(filtergruppenZugriff == null){
			filtergruppenZugriff = new FiltergruppenZugriffsManager();
		}
	}
	
	public void run() { 
		filtergruppe.setLetzteerstellung(DateUtils.asDate(aktuelleZeit));
		
		List<Filterprofil> filterprofile = new ArrayList<Filterprofil>(filtergruppe.getFilterprofile());
		
		List<Artikel> artikel =  solr.sucheArtikel(filterprofile);
		Set<Artikel> artikelAlsSet = new HashSet<Artikel>(artikel);
		
		Archiveintrag archiveintrag = new Archiveintrag(filtergruppe, DateUtils.asDate(aktuelleZeit), artikelAlsSet);
		filtergruppe.getArchiveintraege().add(archiveintrag);
				 
		filtergruppenZugriff.speicherArchiveintrag(archiveintrag, filtergruppe);
		
		//TODO generiere PDF hier
		byte [] pdf = new byte [0];
		
		sendeMailAnEmfaenger(getNutzerHtmlEmpfang() ,true, archiveintrag, pdf);
		sendeMailAnEmfaenger(getNutzerPlaintextEmpfang(),false, archiveintrag, pdf);
	}
	
	/**
	 * Sendet eine Benachrichtigung über den generierten Archiveintrag an alle angegebenen Nutzer.
	 * @param empfaenger Empfänger der Benachrichtigung
	 * @param html Soll der Inhalt der Email als HTML formatiert werden.
	 * @param archiveintrag Der Archiveintrag über den Informiert wird.
	 * @param pdf Das PDF mit dem Inhalt des Archiveintrags.
	 */
	private void sendeMailAnEmfaenger(
			List<Nutzer> empfaenger,
			boolean html, 
			Archiveintrag archiveintrag,
			byte [] pdf){ 
		
		if(empfaenger.isEmpty()) return;
		
		String body;
		String betreff = "SiteScrawler zusammenfassung von " +  filtergruppe.getTitel() + " vom " + aktuelleZeit.format(DateUtils.getDateFormatter());
		
		//Generiere die Zusammenfassung je nach Einstellung
		if(html)
		{
			body = formatiererService.generiereHtmlZusammenfassung(archiveintrag);
		}
		else
		{
			body = formatiererService.generierePlaintextZusammenfassung(archiveintrag);
		}
		
		mailSenderService.sendeMassenMail(empfaenger, betreff, body, html, pdf);
	} 
	
	
	/**
	 * Gibt alle Empfänger zurück, die ihre Mail mit HTML Elementen empfangen wollen.
	 * @return html Empfänger.
	 */
	private List<Nutzer> getNutzerHtmlEmpfang(){ 
		List<Nutzer> alleNutzer = new ArrayList<Nutzer>(filtergruppe.getEmpfaenger());
		
		//TODO HTML/Plain eigenschaft fehlt noch
		
		return alleNutzer;
	}
	
	/**`
	 * Gibt alle Empfänger zurück, die ihre Mail im Plaintext empfangen wollen.
	 * @return Plaintext Empfänger.
	 */
	private List<Nutzer> getNutzerPlaintextEmpfang(){ 
		List<Nutzer> alleNutzer = new ArrayList<Nutzer>(filtergruppe.getEmpfaenger());
		
		//TODO HTML/Plain eigenschaft fehlt noch
		
		return alleNutzer;
	}
}
