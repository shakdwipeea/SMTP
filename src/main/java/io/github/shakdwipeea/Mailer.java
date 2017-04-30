package io.github.shakdwipeea;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

/**
 * Created by akash on 17/1/16.
 */
public class Mailer {
    private String username = "";
    private String password = "";

    public Mailer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String receiver, String body, String subject) {
        sendEmail(receiver, body, subject, null);
    }

    /**
     *
     * @param receiver Email address of the person receiveing mail
     * @param body Body of the email
     * @param subject Subject of the email
     * @param fileNames FileNames to be added as attachment. Must be full paths
     */
    public void sendEmail(String receiver, String body, String subject, ArrayList<String> fileNames) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            setupMultipart();

            /**
             * Prepare the message
             */
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiver));
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(body);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Add attachments if available
            if (fileNames != null) {
                // Part two is attachment
                for (String fileName : fileNames) {
                    multipart.addBodyPart(addAttachments(fileName));
                }
            }

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupMultipart() {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
    }

    /**
     *
     * @param fileName File to be added as attachment
     * @return MimeBodyPart to be added in multipart message
     * @throws MessagingException
     */
    private MimeBodyPart addAttachments(String fileName) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(fileName);
        messageBodyPart.setDataHandler(new DataHandler(dataSource));
        messageBodyPart.setFileName(fileName);
        return messageBodyPart;
    }
}
