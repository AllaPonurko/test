package org.example.response;

import org.example.entity.product.Book;

public class BookResponse extends BaseResponse<Book> {
    public BookResponse(String message, Book entity) {
        super(message, entity);
    }
}
