package org.example.event;

import org.example.entity.product.Book;
import org.springframework.context.ApplicationEvent;


public class BookCreatedEvent extends ApplicationEvent {
    private final Book book;
    public BookCreatedEvent(Object source, Book book) {
        super(source);
        this.book = book;
    }
    public Book getBook(){
        return book;
    }
}
