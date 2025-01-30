package org.example.services.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.example.dto.BaseReq;
import org.example.events.BookCreatedEvent;
import org.example.models.products.Book;
import org.example.repositories.BookRepository;
import org.example.services.interfaces.IBookService;
import org.example.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService extends BaseService<Book> implements IProductService <Book, BaseReq>, IBookService<Book> {
    private List<Book> books;
    @Value("${book.data.file}")
    private String bookDataFile;
    @Autowired
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher eventPublisher;
    public BookService(BookRepository bookRepository, ApplicationEventPublisher eventPublisher){
        this.bookRepository=bookRepository;
        this.eventPublisher = eventPublisher;
        this.repository = bookRepository;

    }
    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        books=readFromJsonFile(bookDataFile,bookRepository);
    }
    @Override
    public Optional<Book> findById(String id) {
        UUID bookId = UUID.fromString(id);
        return bookRepository.findById(bookId);
    }
    @Override
    public List<Book> findByAuthor(String author) {
        return List.of();
    }
    @Override
    public List<Book> findByGenre(String genre) {
        return List.of();
    }

    @Override
    @Transactional
    public Book createProduct(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if(!baseReq.name().isEmpty()&&!baseReq.genre().isEmpty()
                &&!baseReq.author().isEmpty()&& !(baseReq.price() ==0)) {
            Book book = new Book(baseReq.name(), baseReq.price(),
                    baseReq.description(), baseReq.genre(), baseReq.author());
            book.setAvailable(true);
            eventPublisher.publishEvent(new BookCreatedEvent(this,book));
            try {
                addEntity(book, bookDataFile, bookRepository);
                return book;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving book data to file: " + e.getMessage());
            }
        }
        return null;
    }
    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }
}
