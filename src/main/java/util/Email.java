package util;

import java.util.Properties;

public class Email {
    private static final String HOST = "127.0.0.1";

    public static void sendEmail(String to, String from, String text) {

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);

    }
}
