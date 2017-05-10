package de.sitescrawler.email;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import de.sitescrawler.email.interfaces.IMailSenderService;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.ProjectConfig;

@ApplicationScoped
public class MailSenderService implements IMailSenderService
{

    @Override
    public void sendeMail(String emailAdresse, String subjekt, String body, boolean htmlBody, List<byte[]> anhaenge) throws ServiceUnavailableException
    {

        // Project Config laden f�r username/password
        ProjectConfig projectConfig = new ProjectConfig();

        String username = projectConfig.getUsername();
        String password = projectConfig.getPassword();

        Properties props = System.getProperties();
        String host = "smtp.outlook.com";

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props); // getInstance statt getDefaultInstance (neue convention)

        MimeMessage nachricht = new MimeMessage(session);

        try
        {
            nachricht.setFrom(new InternetAddress(username));
            nachricht.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAdresse));
            nachricht.setSubject(subjekt);

            // Teil eins ist die Nachricht
            MimeBodyPart nachrichtTeil = new MimeBodyPart();
            if (htmlBody)
            {
                nachrichtTeil.setContent(body, "text/html");
            }
            else
            {
                nachrichtTeil.setText(body);
            }

            // Erstelle eine multipar nachricht
            Multipart multipart = new MimeMultipart();
            // Setze text Nachricht Teil
            multipart.addBodyPart(nachrichtTeil);

            // Teil zwei ist Anhang
            for (int i = 0; i < anhaenge.size(); i++)
            {
                MimeBodyPart anhang = new MimeBodyPart();
                DataSource bds = new ByteArrayDataSource(anhaenge.get(i), "application/pdf");
                anhang.setDataHandler(new DataHandler(bds));
                anhang.setFileName("Pressespiegel Nr." + i);
                // Setze Anhang Teil
                multipart.addBodyPart(anhang);
            }

            // Zusammenf�hren der Teile
            nachricht.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(nachricht, nachricht.getAllRecipients());
            transport.close();

            System.out.println("Erfolgreich versendet"); // TODO: Logging

        }
        catch (AddressException ae)
        {
            // TODO: Logging
            ae.printStackTrace();
        }
        catch (MessagingException e)
        {
            System.out.println("Fehler beim versenden"); // TODO: Logging
            e.printStackTrace();
        }

    }

    @Override
    public void sendeMassenMail(List<Nutzer> empfaenger, String subjekt, String body, boolean htmlBody, byte[] pdf) throws ServiceUnavailableException
    {

        // Project Config laden f�r username/password
        ProjectConfig projectConfig = new ProjectConfig();

        String username = projectConfig.getUsername();
        String password = projectConfig.getPassword();

        Properties props = System.getProperties();
        String host = "smtp.outlook.com";

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props); // getInstance statt getDefaultInstance (neue convention)

        MimeMessage nachricht = new MimeMessage(session);

        try
        {
            nachricht.setFrom(new InternetAddress(username));

            InternetAddress[] toAddress = new InternetAddress[empfaenger.size()];

            for (int i = 0; i < empfaenger.size(); i++)
            {
                toAddress[i] = new InternetAddress(empfaenger.get(i).getEmail());
            }

            for (int i = 0; i < toAddress.length; i++)
            {
                nachricht.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            nachricht.setSubject(subjekt);

            // Teil eins ist die Nachricht
            BodyPart nachrichtTeil = new MimeBodyPart();
            if (htmlBody)
            {
                nachrichtTeil.setContent(body, "text/html");
            }
            else
            {
                nachrichtTeil.setText(body);
            }

            // Erstelle eine multipar nachricht
            Multipart multipart = new MimeMultipart();
            // Setze text Nachricht Teil
            multipart.addBodyPart(nachrichtTeil);

            // Teil zwei ist Anhang

            MimeBodyPart anhang = new MimeBodyPart();
            DataSource bds = new ByteArrayDataSource(pdf, "application/pdf");
            anhang.setDataHandler(new DataHandler(bds));
            anhang.setFileName("Pressespiegel");
            // Setze Anhang Teil
            multipart.addBodyPart(anhang);

            // Zusammenf�hren der Teile
            nachricht.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(nachricht, nachricht.getAllRecipients());
            transport.close();
            System.out.println("Erfolgreich versendet"); // TODO: Logging

        }
        catch (AddressException ae)
        {
            // TODO:Logging
            ae.printStackTrace();
        }
        catch (MessagingException e)
        {
            System.out.println("Fehler beim versenden"); // TODO: Logging
            e.printStackTrace();
        }

    }
}
