package org.example.service.notification;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.EmailReq;
import org.example.entity.product.Product;
import org.example.entity.user.User;
import org.example.entity.product.Book;
import org.example.enums.EmailSendTypeEnum;
import org.example.event.EmailSendEvent;
import org.example.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BookNotificationService {
    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final MailtrapClient mailtrapClient;


    public BookNotificationService(EmailService emailService, UserService userService, MailtrapClient mailtrapClient) {
        this.emailService = emailService;
        this.userService = userService;
        this.mailtrapClient = mailtrapClient;
    }

    @Transactional
    public void sendNotificationToUsers(Product product) {
        List<User> userList = userService.getUsers();
        List<User> usersForSending = new ArrayList<>();
        if(product instanceof Book) {
            AtomicReference<Book> book = new AtomicReference<>(new Book());
            userList.forEach(user -> {
                if ( product.isAvailable()){
                    book.set((Book) product);
                    if (isInterestTopicByGenre(book.get().getGenre().getEnumValue().getValue(), user)) {
                        usersForSending.add(user);
                    }
                }
            });
            if (!usersForSending.isEmpty()) {
                usersForSending.forEach(user -> {
                    EmailReq emailReq = new EmailReq(user.getEmail(), "New book", "New Book Available: " + book.get().getName());
                    mailtrapClient.sendMessage(emailReq, EmailSendTypeEnum.AUTO_BY_EVENT.getValue());
                });
                LOGGER.info("Sending is finished.");
            }
        }
    }

    public boolean isInterestTopicByGenre(String genre, User user) {
        return user.getListOfInterested()
                .stream()
                .anyMatch(interestTopic -> interestTopic.getTopic().getValue().equals(genre));
    }
}
