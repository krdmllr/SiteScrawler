package de.sitescrawler.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Address;
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
import de.sitescrawler.exceptions.ServiceUnavailableException;
import de.sitescrawler.jpa.Nutzer;
import de.sitescrawler.model.ProjectConfig;

/**
 *
 * @author robin Versendet Mails an Empf�nger als HTML/Plain Text mit Anh�ngen
 *
 */

@ApplicationScoped
public class MailSenderService implements IMailSenderService
{

    private Properties    props = System.getProperties();
    private String        username;
    private String        password;
    private String        host;

    @Inject
    private ProjectConfig projectConfig;

    @PostConstruct
    void init()
    {
        this.username = this.projectConfig.getUsername();
        this.password = this.projectConfig.getPassword();
        this.host = "smtp.gmail.com";

        this.props.put("mail.smtp.starttls.enable", "true");
        this.props.put("mail.smtp.host", this.host);
        this.props.put("mail.smtp.user", this.username);
        this.props.put("mail.smtp.password", this.password);
        this.props.put("mail.smtp.port", "587");
        this.props.put("mail.smtp.auth", "true");
    }

    private final static Logger LOGGER = Logger.getLogger("de.sitescrawler.logger");

    /**
     * Versendet Mail an einen Empf�nger
     */
    @Override
    public void sendeMail(String emailAdresse, String subjekt, String body, boolean htmlBody, ByteArrayDataSource anhaenge) throws ServiceUnavailableException
    {
        this.manuellerInject();
        List<String> empfaenger = new ArrayList<>();
        empfaenger.add(emailAdresse);

        this.erstelleUndVerschickeNachricht(empfaenger, subjekt, body, htmlBody, anhaenge);
    }

    /**
     * Sendet Mail an eine Liste von Empf�ngern
     */
    @Override
    public void sendeMail(List<Nutzer> empfaenger, String subjekt, String body, boolean htmlBody, ByteArrayDataSource anhaenge)
        throws ServiceUnavailableException
    {
        this.manuellerInject();

        List<String> empfaengerAdressen = empfaenger.stream().map(Nutzer::getEmail).collect(Collectors.toList());

        this.erstelleUndVerschickeNachricht(empfaengerAdressen, subjekt, body, htmlBody, anhaenge);
    }

    /**
     * Au�erhalb der Server Umgebung, falls Inject nicht funktioniert
     */
    private void manuellerInject()
    {
        if (this.projectConfig == null)
        {
            this.projectConfig = new ProjectConfig();
            this.username = this.projectConfig.getUsername();
            this.password = this.projectConfig.getPassword();
            this.host = "smtp.gmail.com";

            this.props.put("mail.smtp.starttls.enable", "true");
            this.props.put("mail.smtp.host", this.host);
            this.props.put("mail.smtp.user", this.username);
            this.props.put("mail.smtp.password", this.password);
            this.props.put("mail.smtp.port", "587");
            this.props.put("mail.smtp.auth", "true");
        }
    }

    /**
     * Erstellt und Verschickt die Nachricht an die Empf�nger
     *
     * @param emailAdresse
     *            email der empf�nger
     * @param subjekt
     *            Betreff der Mail
     * @param body
     *            Body der Mail (Text oder HTML)
     * @param htmlBody
     *            gibt an ob Body Text oder HTML ist
     * @param anhaenge
     *            Anh�nge die hinzugef�gt werden m�ssen
     */
    private void erstelleUndVerschickeNachricht(List<String> emailAdresse, String subjekt, String body, boolean htmlBody, ByteArrayDataSource anhaenge)
    {
        Session session = Session.getInstance(this.props);
        List<MimeMessage> nachrichten = new ArrayList<>();
        for (String empfaenger : emailAdresse)
        {
            try
            {
                nachrichten.add(this.erstelleNachricht(session, empfaenger, subjekt, body, htmlBody, anhaenge));
            }
            catch (MessagingException e)
            {
                MailSenderService.LOGGER.log(Level.SEVERE, "Fehler beim erstellen und Verschicken der Nachricht.");
                e.printStackTrace();
            }
        }
        this.sendeNachricht(nachrichten, session);
    }

    /**
     * Erstellt die einzelnen Teile der Nachricht und f�gt diese in einer Nachricht zusammen.
     *
     * @param session
     * @param emailAdresse
     *            Email des Empf�ngers
     * @param subjekt
     *            Betreff der Mail
     * @param body
     *            Body der Mail (Text oder HTML)
     * @param htmlBody
     *            gibt an ob Body Text oder HTML ist
     * @param pdfAnhang
     *            Anh�nge die hinzugef�gt werden m�ssen
     * @return
     * @throws AddressException
     * @throws MessagingException
     */
    private MimeMessage erstelleNachricht(Session session, String emailAdresse, String subjekt, String body, boolean htmlBody, ByteArrayDataSource pdfAnhang)
        throws AddressException, MessagingException
    {

        MimeMessage nachricht = new MimeMessage(session);
        nachricht.setFrom(new InternetAddress(this.username));
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

        if (pdfAnhang != null)
        {
            // Teil zwei ist Anhang
            MimeBodyPart anhang = new MimeBodyPart();
            anhang.setDataHandler(new DataHandler(pdfAnhang));
            anhang.setFileName(pdfAnhang.getName());
            multipart.addBodyPart(anhang);
        }

        // Zusammenführen der Teile
        nachricht.setContent(multipart);

        return nachricht;
    }

    /**
     * Baut den Transport auf und sendet die Nachricht
     *
     * @param nachrichten
     *            Zu sendenden Nachrichten
     * @param session
     *            derzeitige Session
     */
    private void sendeNachricht(List<MimeMessage> nachrichten, Session session)
    {

        try
        {

            Transport transport = session.getTransport("smtp");

            transport.connect(this.host, this.username, this.password);

            MailSenderService.LOGGER.log(Level.INFO, "Verbindung zu Mailserver ge�ffnet.");

            for (MimeMessage nachricht : nachrichten)
            {
                Address[] adressen = nachricht.getAllRecipients();
                transport.sendMessage(nachricht, adressen);

                String alleAdressen = "";
                for (Address s : adressen)
                {
                    alleAdressen += s + "; ";
                }
                MailSenderService.LOGGER.log(Level.INFO, "Email versendet an: " + alleAdressen);
            }

            transport.close();
            MailSenderService.LOGGER.log(Level.INFO, "Verbindung zu Mailserver geschlossen.");
        }
        catch (MessagingException e)
        {
            MailSenderService.LOGGER.log(Level.SEVERE, "Fehler beim Senden der Nachricht.");
            e.printStackTrace();
        }
    }
}