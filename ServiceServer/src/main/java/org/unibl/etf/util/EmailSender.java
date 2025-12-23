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
     * @param to             primalac
     * @param subject        naslov mail-a
     * @param body           sadržaj mail-a
     * @param attachmentPath putanja do fajla koji se šalje kao attachment (može biti null)
     */
    public static void sendEmail(String to, String subject, String body, String attachmentPath) {
        final String username = Config.get("mail.username");
        final String password = Config.get("mail.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", Config.get("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", Config.get("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", Config.get("mail.smtp.host"));
        props.put("mail.smtp.port", Config.get("mail.smtp.port"));

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
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

            Transport.send(message);

            System.out.println("Email sent!");

        } catch (MessagingException e) {
        	System.err.println("SMTP probably is not allowed on your network.");
            e.printStackTrace();
        }
    }
}
