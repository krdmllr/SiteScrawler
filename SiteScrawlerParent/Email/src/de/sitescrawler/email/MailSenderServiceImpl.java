package de.sitescrawler.email;

import java.io.IOException;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class MailSenderServiceImpl {

	void sendeMail(String emailAdresse, String subjekt, String body, boolean htmlBody, List<byte[]> anhaenge)
			throws ServiceUnavialableException {
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
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage nachricht = new MimeMessage(session);
			nachricht.setFrom(new InternetAdress(sender));
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
			System.out.println("Erfolgreich versendet"); // TODO: Logging

		} catch (MessagingException e) {
			System.out.println("Fehler beim versenden"); // TODO: Logging
			e.printStackTrace();
		}

	}

	void sendeMassenMail(List<String> emailAdressen, String subjekt, String body, boolean htmlBody,
			List<byte[]> anhaenge) throws ServiceUnavialableException {
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
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage nachricht = new MimeMessage(session);
			nachricht.setFrom(new InternetAdress(sender));
			nachricht.addRecipients(Message.RecipientType.TO, new InternetAddress(emailAdressen));
			nachricht.setSubject(subjekt);

			// nachrichtTeil ist die Nachricht
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
