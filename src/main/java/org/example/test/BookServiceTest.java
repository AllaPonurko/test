package org.example.test;

import org.example.dto.BaseReq;
import org.example.helper.TestHelper;
import org.example.models.products.Book;
import org.example.services.services.BookService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.Test;

@SpringBootTest
public class BookServiceTest {
    @MockBean
    private BookService bookService;
    @Test
    public void createBookTest()throws Exception{
        Book mockBook = new Book();
        mockBook.setName("Mock title");
        var newBookreq= new BaseReq("NewBook",
                21.00,
                "this is a new book of well-known author",
                "book",
                "Old author",
                "New genre",
                "");
        Mockito.when(bookService.createProduct(Mockito.any(BaseReq.class))).thenReturn(mockBook);
      Book newBook=bookService.createProduct(newBookreq);
      Assertions.assertEquals("Mock Title", mockBook.getName());
      Assertions.assertTrue(newBook!=null,"Book is created successful");
    }
}
