package de.sitescrawler.test.unittests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.sitescrawler.email.MailSenderService;
import de.sitescrawler.email.ServiceUnavailableException;
import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.jpa.Nutzer;

public class EmailUnitTest
{
	private final String emailAdresse = "sitescrawler@spoofmail.de";
	
	@Test
	public void sende_einzel_mail(){ 
		String subjekt = "Testtitel";
		String body = "testbody";
		boolean htmlBody = false;
		
		 
		byte[] data = new byte[1460];
		 
		
		IMailSenderService mail = new MailSenderService();
		try {
			mail.sendeMail(emailAdresse,subjekt,body,htmlBody,data);
		} catch (ServiceUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sende_massen_mail(){ 
		List<Nutzer> emailAdressen = new ArrayList<>();
		Nutzer nutzer = new Nutzer();
		nutzer.setEmail(emailAdresse);
		emailAdressen.add(nutzer);
		emailAdressen.add(nutzer);
		
		String subjekt = "Testtitel";
		String body = "testbody";
		boolean htmlBody = false;
		
		 
		byte[] data = new byte[1460];
		 
		
		IMailSenderService mail = new MailSenderService();
		try {
			mail.sendeMail(emailAdressen,subjekt,body,htmlBody,data);
		} catch (ServiceUnavailableException e) {
			e.printStackTrace();
		}
	}
}
