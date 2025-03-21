package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.EmailReq;
import org.example.event.UserCreatedEvent;
import org.example.entity.user.User;
import org.example.service.notification.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCreateListener {
    private final EmailService emailService;

    public UserCreateListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("User created: " + user.toString());
        EmailReq emailReq=new EmailReq(user.getEmail(),"Registration","Registration was successful: "+user.toString());
        emailService.sendEmail(emailReq);
    }
}
