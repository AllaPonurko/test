package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.BaseReq;
import org.example.enums.GenreType;
import org.example.enums.ProductType;
import org.example.event.BookCreatedEvent;
import org.example.handler.ShoWebSocketHandler;
import org.example.entity.product.Book;
import org.example.repository.BookRepository;
import org.example.service.interfaces.IBookService;
import org.example.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService extends BaseService<Book> implements IProductService<Book, BaseReq>, IBookService<Book> {
    private List<Book> books;
    @Value("${book.data.file}")
    private String bookDataFile;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final ShoWebSocketHandler webSocketHandler;

    private static final Logger LOGGER = LogManager.getLogger();
    private final ApplicationEventPublisher eventPublisher;

    public BookService(BookRepository bookRepository, ShoWebSocketHandler webSocketHandler, ApplicationEventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.webSocketHandler = webSocketHandler;
        this.eventPublisher = eventPublisher;
        this.repository = bookRepository;

    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        books = readFromJsonFile(bookDataFile, bookRepository);
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
       var books=bookRepository.findAllByGenre(genre) ;
       if(books.isEmpty())
        return List.of();
       return  books;
    }

    @Override
    @Transactional
    public Book createProduct(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if (!baseReq.name().isEmpty() && baseReq.genre() != 0
                && !baseReq.author().isEmpty() && !(baseReq.price() == 0)) {
            String genre = "Not defined";
            switch (baseReq.genre()) {
                case 1:
                    genre = GenreType.DRAMA.toString();
                    break;
                case 2:
                    genre = GenreType.ADVENTURES.toString();
                    break;
                case 3:
                    genre = GenreType.DETECTIVE.toString();
                    break;
                case 4:
                    genre = GenreType.SCIENCE_FICTION_GENRE.toString();
                    break;
                case 5:
                    genre = GenreType.NOVEL.toString();
                    break;
                case 6:
                    genre = GenreType.POETRY.toString();
                    break;
                case 7:
                    genre = GenreType.SHORT_STORY.toString();
                    break;
            }
            Book book = new Book(baseReq.name(), baseReq.price(),
                    baseReq.description(), genre, baseReq.author());
            book.setAvailable(true);
            book.setProductType(ProductType.BOOK);
            LOGGER.info("Book is created successfully " + book.toString());
            eventPublisher.publishEvent(new BookCreatedEvent(this, book));
            try {
                addEntity(book, bookDataFile, bookRepository);
                LOGGER.info("Book is added successfully ");
                return book;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving book data to file: " + e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public boolean deleteBook(UUID uuid) {
        boolean isBookDelete = false;
        Optional<Book> book = bookRepository.findById(uuid);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            isBookDelete = true;
        } else LOGGER.warn("Book with id " + uuid + " is not exist");
        return isBookDelete;
    }

    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }
}
