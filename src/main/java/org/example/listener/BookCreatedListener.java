package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.controller.ChatController;
import org.example.event.BookCreatedEvent;
import org.example.entity.product.Book;
import org.example.service.notification.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BookCreatedListener {
    private final EmailService emailService;
    private final ChatController chatController;

    public BookCreatedListener(EmailService emailService, ChatController chatController) {
        this.emailService = emailService;
        this.chatController = chatController;
    }

    @EventListener
    public void onBookCreated(BookCreatedEvent event) throws IOException {
        Book book = event.getBook();
        chatController.sendNewBookNotification(book);
        // emailService.sendEmail("aponurko@gmail.com", "New book: ", book.toString());
    }
}
