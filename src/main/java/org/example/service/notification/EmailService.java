package org.example.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value(value = "${own.email}")
    private String myEmail;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(myEmail);
        mailSender.send(message);
    }
}
