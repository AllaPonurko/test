package org.example.service.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.BaseReq;
import org.example.enums.GenreTypeEnum;
import org.example.enums.ProductTypeEnum;
import org.example.enums.TypeOfChangesEnum;
import org.example.event.BookCreatedEvent;
import org.example.event.EntityChangedEvent;
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

    @Value("${book.data.file}")
    private String bookDataFile;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final ShoWebSocketHandler webSocketHandler;

    private static final Logger LOGGER = LogManager.getLogger();
    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    public BookService(BookRepository bookRepository, ShoWebSocketHandler webSocketHandler, ApplicationEventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.webSocketHandler = webSocketHandler;
        this.eventPublisher = eventPublisher;
        this.repository = bookRepository;

    }

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException {
        //books = readFromJsonFile(bookDataFile, bookRepository);
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
    public Book createItem(BaseReq baseReq) throws IOException, ClassNotFoundException {
        if (!baseReq.name().isEmpty() && baseReq.genre() != 0
                && !baseReq.author().isEmpty() && !(baseReq.price() !=null)) {
            String genre = "";
            switch (baseReq.genre()) {
                case 1:
                    genre = GenreTypeEnum.DRAMA.toString();
                    break;
                case 2:
                    genre = GenreTypeEnum.ADVENTURES.toString();
                    break;
                case 3:
                    genre = GenreTypeEnum.DETECTIVE.toString();
                    break;
                case 4:
                    genre = GenreTypeEnum.SCIENCE_FICTION_GENRE.toString();
                    break;
                case 5:
                    genre = GenreTypeEnum.NOVEL.toString();
                    break;
                case 6:
                    genre = GenreTypeEnum.POETRY.toString();
                    break;
                case 7:
                    genre = GenreTypeEnum.SHORT_STORY.toString();
                    break;
                default:
                    genre="Not defined";
                    break;
            }
            Book book = new Book(baseReq.name(), baseReq.price(),
                    baseReq.description(), genre, baseReq.author());
            book.setAvailable(true);
            book.setProductType(ProductTypeEnum.BOOK);
            LOGGER.info("Book is created successfully " + book.toString());
            eventPublisher.publishEvent(new BookCreatedEvent(this, book));
            try {
                addEntity(book, bookRepository);
                LOGGER.info("Book with {}",book.getId()+" is added successfully ");
                eventPublisher.publishEvent(new EntityChangedEvent(book, TypeOfChangesEnum.CREATED_BY_ADMIN.getValue()));
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
            eventPublisher.publishEvent(new EntityChangedEvent(book, TypeOfChangesEnum.MANUAL_DELETED.getValue()));
        } else LOGGER.warn("Book with id " + uuid + " is not exist");
        return isBookDelete;
    }

    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }
}
