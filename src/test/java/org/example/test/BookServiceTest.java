package org.example.test;

import org.example.App;
import org.example.dto.BaseReq;
import org.example.entity.product.Book;
import org.example.service.service.BookService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;


@SpringBootTest(classes = App.class)
public class BookServiceTest {
    @MockBean
    private BookService bookService;
    @Test
    public void createBookTest()throws Exception{
        Book mockBook = new Book();
        mockBook.setName("Mock title");
        var newBookreq= new BaseReq("NewBook",
                new BigDecimal("21.00"),
                "this is a new book of well-known author",
                (long)1L,
                "Old author",
                (long) 4L,
                "");
        Mockito.when(bookService.createItem(Mockito.any(BaseReq.class))).thenReturn(mockBook);
      Book newBook=bookService.createItem(newBookreq);
      Assertions.assertEquals("Mock title", mockBook.getName());
      Assertions.assertTrue(newBook!=null,"Book is created successful");
    }
}
