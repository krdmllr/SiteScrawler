package de.sitescrawler.email;

import java.util.ArrayList;
import java.util.List;

import de.sitescrawler.email.interfaces.IMailSenderService;

/**
 * 
 * @author robin Wird zum manuellen Versenden von Mails zu Testzwecken genutzt.
 */
public class Main {

	public static void main(String[] args) {

		String emailAdresse = "sitescrawler@spoofmail.de";
		String subjekt = "Testtitel";
		String body = "testbody";
		boolean htmlBody = false;

		byte[] data = new byte[1460];

		IMailSenderService mail = new MailSenderService();
		try {
			mail.sendeMail(emailAdresse, subjekt, body, htmlBody, data);
		} catch (ServiceUnavailableException e) {
			e.printStackTrace();
		}
	}

}
