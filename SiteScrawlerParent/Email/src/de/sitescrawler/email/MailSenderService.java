package de.sitescrawler.email;

import java.util.List;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.jpa.Nutzer;

public class MailSenderService implements IMailSenderService {

	@Override
	public void sendeMail(String emailAdresse, String subjekt, String body, boolean htmlBody, List<byte[]> anhaenge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendeMassenMail(List<Nutzer> empfaenger, String subjekt, String body, boolean htmlBody, byte[] pdf) {
		// TODO Auto-generated method stub
		
	} 

}
