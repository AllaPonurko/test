package org.example.repository;

import org.example.entity.product.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByAuthor(String author);
    @Query(value = "select book from Book book where book.genre=:genre")
    List<Book> findAllByGenre(String genre);
}
