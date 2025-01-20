package org.example.responses;

import org.example.models.products.Book;

public class BookResponse extends BaseResponse<Book> {
    public BookResponse(String message, Book entity) {
        super(message, entity);
    }
}
