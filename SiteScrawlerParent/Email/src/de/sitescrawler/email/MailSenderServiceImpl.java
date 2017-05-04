package de.sitescrawler.email;

import java.io.IOException;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import de.sitescrawler.email.interfaces.IMailSenderService;

public class MailSenderServiceImpl implements IMailSenderService{

	@Override
	public void sendeMail(String emailAdresse, String subjekt, String body, boolean htmlBody, List<byte[]> anhaenge) throws ServiceUnavailableException{
		// localhost, da noch kein Mailserver
		String host = "localhost";

		// Sender Mail
		String sender = "beispielemail@gmail.com";

		Properties props = System.getProperties();

		// Mail Server initialisieren
		props.put("mail.smtp.host", host);

		// Falls benötigt
		props.setProperty("mail.user", "username");
		props.setProperty("mail.password", "password");

		// Default Session
		Session session = Session.getDefaultInstance(props);

		try {
			MimeMessage nachricht = new MimeMessage(session);
			nachricht.setFrom(new InternetAddress(sender));
			nachricht.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAdresse));
			nachricht.setSubject(subjekt);

			// Teil eins ist die Nachricht
			MimeBodyPart nachrichtTeil = new MimeBodyPart();
			if (htmlBody)
				nachrichtTeil.setContent(body, "text/html");
			else
				nachrichtTeil.setText(body);

			// Teil zwei ist Anhang
			MimeBodyPart anhang = new MimeBodyPart();
			DataSource dataSource = new ByteArrayDataSource(anhaenge, "application/pdf");
			anhang.setDataHandler(new DataHandler(dataSource));
			anhang.setFileName("Pressespiegel");

			// Erstelle eine multipar nachricht
			Multipart multipart = new MimeMultipart();
			// Setze text Nachricht Teil
			multipart.addBodyPart(nachrichtTeil);
			// Setze Anhang Teil
			multipart.addBodyPart(anhang);

			// Zusammenführen der Teile
			nachricht.setContent(multipart);

			Transport.send(nachricht);
			System.out.println("Erfolgreich versendet"); // TODO: Logging

		} catch (MessagingException e) {
			System.out.println("Fehler beim versenden"); // TODO: Logging
			e.printStackTrace();
		}

	}

	@Override
	public void sendeMassenMail(List<String> emailAdressen, String subjekt, String body, boolean htmlBody,
			List<byte[]> anhaenge) throws ServiceUnavailableException {
		
		// localhost, da noch kein Mailserver
		String host = "localhost";

		// Sender Mail
		String sender = "beispielemail@gmail.com";

		Properties props = System.getProperties();

		// Mail Server initialisieren
		props.put("mail.smtp.host", host);

		// Falls benötigt
		props.setProperty("mail.user", "username");
		props.setProperty("mail.password", "password");

		// Default Session
		Session session = Session.getDefaultInstance(props);

		try {
			MimeMessage nachricht = new MimeMessage(session);
			nachricht.setFrom(new InternetAddress(sender));
			
			InternetAddress[] toAddress = new InternetAddress[emailAdressen.size()];
			for(int i = 0; i < emailAdressen.size(); i++)
			{
				toAddress[i] = new InternetAddress(emailAdressen.get(i));
			}
			nachricht.addRecipients(Message.RecipientType.TO, toAddress);
			nachricht.setSubject(subjekt);

			// Teil eins ist die Nachricht
			BodyPart nachrichtTeil = new MimeBodyPart();
			if (htmlBody)
				nachrichtTeil.setContent(body, "text/html");
			else
				nachrichtTeil.setText(body);

			// Teil zwei ist Anhang
			MimeBodyPart anhang = new MimeBodyPart();
			DataSource dataSource = new ByteArrayDataSource(anhaenge, "application/pdf");
			anhang.setDataHandler(new DataHandler(dataSource));
			anhang.setFileName("Pressespiegel");

			// Erstelle eine multipar nachricht
			Multipart multipart = new MimeMultipart();
			// Setze text Nachricht Teil
			multipart.addBodyPart(nachrichtTeil);
			// Setze Anhang Teil
			multipart.addBodyPart(anhang);

			// Zusammenführen der Teile
			nachricht.setContent(multipart);

			Transport.send(nachricht);
			System.out.println("Erfolgreich versendet"); // TODO: Logging

		} catch (MessagingException e) {
			System.out.println("Fehler beim versenden"); // TODO: Logging
			e.printStackTrace();
		}
	}

}
