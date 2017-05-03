package de.sitescrawler.email;

import java.util.ArrayList;
import java.util.List;

import de.sitescrawler.email.interfaces.IMailSenderService;

public class Main {

	public static void main(String[] args) {
		// Zu Testzwecken
		
		String emailAdresse = "tesmail@gmail.com";
		String subjekt = "Testtitel";
		String body = "testbody";
		boolean htmlBody = false;
		
		ArrayList anhaenge = new ArrayList();
		byte[] data = new byte[1460];
		anhaenge.add(data);
		
		IMailSenderService mail = new MailSenderServiceImpl();
		try {
			mail.sendeMail(emailAdresse,subjekt,subjekt,htmlBody,anhaenge);
		} catch (ServiceUnavailableException e) {
			e.printStackTrace();
		}
	}

}
