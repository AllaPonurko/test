package org.example.helper;

import org.example.dto.BaseReq;
import org.example.entity.product.Book;
import org.example.service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TestHelper {
    @Autowired
    private final BookService bookService;
    public TestHelper(BookService bookService) {
        this.bookService = bookService;
    }
    public Book createBook() throws IOException, ClassNotFoundException {
       var newBook= new BaseReq("NewBook",
                21.00,
                "this is a new book of well-known author",
                1,
                "Old author",
               "New genre",
               "");
        return  bookService.createProduct(newBook);
    }
}
