package util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {
    private static final String HOST = "127.0.0.1";

    public static void sendEmail(String to, String from, String subject, String text) {

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }

    }
}
