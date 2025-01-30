package org.example.test;

import org.example.helper.TestHelper;
import org.example.models.products.Book;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.Test;

@SpringBootTest
public class BookServiceTest {
    @MockBean
    private TestHelper testHelper;
    @Test
    public void createBookTest()throws Exception{
        Book mockBook = new Book();
        mockBook.setName("Mock title");
        Mockito.when(testHelper.createBook()).thenReturn(mockBook);
      Book newBook=testHelper.createBook();
      Assertions.assertEquals("Mock Title", newBook.getName());
      Assertions.assertTrue(newBook!=null,"Book is created successful");
    }
}
