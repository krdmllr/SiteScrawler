package de.sitescrawler.reporter.implementations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped; 
import javax.inject.Inject;

import de.sitescrawler.email.ServiceUnavailableException;
import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.formatierer.interfaces.IFormatiererService;
import de.sitescrawler.jpa.Archiveintrag;
import de.sitescrawler.jpa.Artikel;
import de.sitescrawler.jpa.Filterprofil;
import de.sitescrawler.jpa.Filterprofilgruppe;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.jpa.management.interfaces.IFiltergruppenZugriffsManager;
import de.sitescrawler.solr.interfaces.ISolrService;
import de.sitescrawler.utility.DateUtils;

@ApplicationScoped
public class ArchiveintragErstellen{

	@Inject
	ISolrService solr;
	
	@Inject
	IMailSenderService mailSenderService;
	
	@Inject
	IFiltergruppenZugriffsManager filtergruppenZugriff;
	
	@Inject
	IFormatiererService formatiererService; 
	
	public void erstelleReport(Filterprofilgruppe filtergruppe,
			LocalDateTime aktuelleZeit){
		filtergruppe.setLetzteerstellung(DateUtils.asDate(aktuelleZeit));
		
		List<Filterprofil> filterprofile = new ArrayList<Filterprofil>(filtergruppe.getFilterprofile());
		
		List<Artikel> artikel =  solr.sucheArtikel(filterprofile);
		Set<Artikel> artikelAlsSet = new HashSet<Artikel>(artikel);
		
		Archiveintrag archiveintrag = new Archiveintrag(filtergruppe, DateUtils.asDate(aktuelleZeit), artikelAlsSet);
		filtergruppe.getArchiveintraege().add(archiveintrag);
				 
		filtergruppenZugriff.speicherArchiveintrag(archiveintrag, filtergruppe);
		
		//TODO generiere PDF hier
		byte [] pdf = new byte [0];
		
		sendeMailAnEmfaenger(filtergruppe, aktuelleZeit, getNutzerHtmlEmpfang(filtergruppe) ,true, archiveintrag, pdf);
		sendeMailAnEmfaenger(filtergruppe, aktuelleZeit, getNutzerPlaintextEmpfang(filtergruppe),false, archiveintrag, pdf);
	}
	
	/**
	 * Sendet eine Benachrichtigung �ber den generierten Archiveintrag an alle angegebenen Nutzer.
	 * @param empfaenger Empf�nger der Benachrichtigung
	 * @param html Soll der Inhalt der Email als HTML formatiert werden.
	 * @param archiveintrag Der Archiveintrag �ber den Informiert wird.
	 * @param pdf Das PDF mit dem Inhalt des Archiveintrags.
	 */
	private void sendeMailAnEmfaenger(
			Filterprofilgruppe filtergruppe,
			LocalDateTime aktuelleZeit,
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
		 
		try {
			mailSenderService.sendeMassenMail(empfaenger, betreff, body, html, pdf);
		} catch (ServiceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	
	/**
	 * Gibt alle Empf�nger zur�ck, die ihre Mail mit HTML Elementen empfangen wollen.
	 * @return html Empf�nger.
	 */
	private List<Nutzer> getNutzerHtmlEmpfang(Filterprofilgruppe filtergruppe){ 
		List<Nutzer> alleNutzer = new ArrayList<Nutzer>(filtergruppe.getEmpfaenger());
		
		//TODO HTML/Plain eigenschaft fehlt noch
		
		return alleNutzer;
	}
	
	/**`
	 * Gibt alle Empf�nger zur�ck, die ihre Mail im Plaintext empfangen wollen.
	 * @return Plaintext Empf�nger.
	 */
	private List<Nutzer> getNutzerPlaintextEmpfang(Filterprofilgruppe filtergruppe){ 
		List<Nutzer> alleNutzer = new ArrayList<Nutzer>(filtergruppe.getEmpfaenger());
		
		//TODO HTML/Plain eigenschaft fehlt noch
		
		return alleNutzer;
	}
}
