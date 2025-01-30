package org.example.listeners;

import lombok.extern.slf4j.Slf4j;
import org.example.events.BookCreatedEvent;
import org.example.models.products.Book;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class BookCreatedListener {
@EventListener
    public void onBookCreated(BookCreatedEvent event){
    Book book=event.getBook();
    System.out.println("Book created: "+book.toString());
}
}
