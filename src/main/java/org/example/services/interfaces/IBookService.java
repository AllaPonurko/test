package org.example.services.interfaces;

import java.util.List;

public interface IBookService <T>{
    List<T> findByAuthor(String author);
    List<T> findByGenre(String genre);
}
