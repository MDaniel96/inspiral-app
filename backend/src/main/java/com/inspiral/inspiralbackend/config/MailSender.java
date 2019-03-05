package com.inspiral.inspiralbackend.config;

import com.inspiral.inspiralbackend.models.EmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Component
public class MailSender {

    @Value("${gmail.username}")
    private String username;

    @Value("${gmail.password}")
    private String password;

    public void sendmail(EmailMessage emailmessage) throws AddressException, MessagingException, IOException {

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

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailmessage.getTo_address()));
        msg.setSubject(emailmessage.getSubject(), "UTF-8");
        msg.setContent(emailmessage.getBody(), "text/html; charset=utf-8");
        msg.setSentDate(new Date());

        /*MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailmessage.getBody(), "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();

        attachPart.attachFile("");

        multipart.addBodyPart(attachPart);
        msg.setContent(multipart); */

        Transport.send(msg);

    }
}
