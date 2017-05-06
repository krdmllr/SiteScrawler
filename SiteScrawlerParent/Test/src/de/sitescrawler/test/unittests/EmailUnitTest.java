package de.sitescrawler.test.unittests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.sitescrawler.email.MailSenderService;
import de.sitescrawler.email.ServiceUnavailableException;
import de.sitescrawler.email.interfaces.IMailSenderService;

public class EmailUnitTest
{
	@Test
	public void sendeMail(){
		String emailAdresse = "skyf1ash3r@gmail.com";
		String subjekt = "Testtitel";
		String body = "testbody";
		boolean htmlBody = false;
		
		List anhaenge = new ArrayList();
		byte[] data = new byte[1460];
		anhaenge.add(data);
		
		IMailSenderService mail = new MailSenderService();
		try {
			mail.sendeMail(emailAdresse,subjekt,body,htmlBody,anhaenge);
		} catch (ServiceUnavailableException e) {
			e.printStackTrace();
		}
	}
}
