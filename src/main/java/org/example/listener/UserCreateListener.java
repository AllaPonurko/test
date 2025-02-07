package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.event.UserCreateEvent;
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
    public void onUserCreated(UserCreateEvent event) {
        User user = event.getUser();
        System.out.println("User created: " + user.toString());
        emailService.sendEmail(user.getEmail(), "Registration was successful: ",user.toString());
    }
}
