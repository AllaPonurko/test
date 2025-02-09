package org.example.service.notification;

import org.example.entity.product.Book;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookNotificationScheduler {
    private final BookNotificationService bookNotificationService;
    private final BookRepository bookRepository;

    List<Book> notifiedBooks = new ArrayList<>();

    @Autowired
    public BookNotificationScheduler(BookNotificationService bookNotificationService, BookRepository bookRepository) {
        this.bookNotificationService = bookNotificationService;
        this.bookRepository = bookRepository;

    }

    @Scheduled(fixedRate = 3000000)
    public void notifyUsersAboutNewBooks() {
        List<Book> newBooks = bookRepository.findAll();
        newBooks.forEach(book -> {
            if (!notifiedBooks.contains(book)) {
                notifiedBooks.add(book);
                bookNotificationService.sendNotificationToUsers(book);
            }
        });
    }
}
