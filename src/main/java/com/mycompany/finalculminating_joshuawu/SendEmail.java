package com.mycompany.finalculminating_joshuawu;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author Sajid
 */ 

public class SendEmail { //class generously given by Akbar to send emails.
    final String senderEmail = "Insert Email Here"; // change email address
    final String senderPassword = "Insert Password Here"; // change password
    final String emailSMTPserver = "smtp.gmail.com";
    final String emailServerPort = "465";
    boolean errorOccured;
    String receiverEmail = null;
    static String emailSubject;
    static String emailBody;

    public SendEmail(String receiverEmail, String subject, String body) {
        // receiver email
        this.receiverEmail = receiverEmail;
        // subject
        this.emailSubject = subject;
        // body
        this.emailBody = body;

        Properties props = new Properties();
        props.put("mail.smtp.user", senderEmail);
        props.put("mail.smtp.host", emailSMTPserver);
        props.put("mail.smtp.port", emailServerPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(emailBody);
            msg.setSubject(emailSubject);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            Transport.send(msg);
            this.errorOccured = false;
            System.out.println("Message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            this.errorOccured = true;
        }
    }

    public class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(senderEmail, senderPassword);
        }
    }
}

