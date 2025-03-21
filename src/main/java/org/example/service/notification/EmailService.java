package org.example.service.notification;

import org.example.dto.EmailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${own.email}")
    private String myEmail;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendEmail(EmailReq emailReq) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailReq.sendTo());
        message.setSubject(emailReq.theme());
        message.setText(emailReq.body());
        message.setFrom(myEmail);
        mailSender.send(message);
    }
}
