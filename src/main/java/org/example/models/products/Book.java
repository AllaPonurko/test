package org.example.models.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name="books")
public class Book extends Product{

    private  String genre;

    private String author;
    //Constructor
    public Book(String name, double price, String description,String genre,String author)
    {
        super(name, price, description);
        this.author=author;
        this.genre=genre;
    }

    public Book() {

    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    @Override
    public String toString() {
        return super.toString() +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
