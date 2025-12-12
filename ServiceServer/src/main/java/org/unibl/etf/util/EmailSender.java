package org.unibl.etf.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Multipart;


import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;


public class EmailSender {

    /**
     * Šalje e-mail sa ili bez attachmenta
     * 
     * @param to             primatelj
     * @param subject        naslov maila
     * @param body           sadržaj maila
     * @param attachmentPath putanja do fajla koji se šalje kao attachment (može biti null)
     */
    public static void sendEmail(String to, String subject, String body, String attachmentPath) {
        final String username = "nutritivnaz@gmail.com";
        final String password = "kocupkwyxmeemfvp";

        // SMTP konfiguracija
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Kreiranje sesije
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // MimeMessage je podklasa Message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Body + attachment
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachmentPath != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(new File(attachmentPath).getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            // Slanje maila
            Transport.send(message);

            System.out.println("Email sent!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Test metoda (primjer poziva)
    public static void main(String[] args) {
        sendEmail("grujo23@gmail.com",
                  "Test Email",
                  "Ovo je test email iz JavaMail sa attachmentom",
                  "C:\\Users\\Administrator\\Desktop\\invoice\\Marko_Marković_2025-12-03_14-16-54.zip");
    }
}
