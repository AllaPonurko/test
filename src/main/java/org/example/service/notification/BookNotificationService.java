package org.example.service.notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.user.User;
import org.example.entity.product.Book;
import org.example.service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookNotificationService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final EmailService emailService;
    private final UserService userService;
    private String sendTo;

    public BookNotificationService(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    public void sendNotificationToUsers(Book book) {
        List<User> userList=userService.getUsers();
        userList.forEach(user ->{
            sendTo=user.getEmail();
            emailService.sendEmail(sendTo,"New Book Available: ", book.getName());
        });
        LOGGER.info("Sending is finished.");
    }
}
