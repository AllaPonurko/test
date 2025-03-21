package org.example.entity.product;

import jakarta.persistence.*;
import org.example.entity.dialer.DialerGenre;

import java.math.BigDecimal;


@Entity
@Table(name = "books")
public class Book extends Product {

    public DialerGenre getGenre() {
        return genre;
    }

    public void setGenre(DialerGenre genre) {
        this.genre = genre;
    }

    @ManyToOne
    private DialerGenre genre;
    @Column(name = "genre")
    private String genreType;
    private String author;

    //Constructor
    public Book(String name, BigDecimal price, String description, DialerGenre genre, String author) {
        super(name, price, description);
        this.author = author;
        this.genre = genre;

    }

    public Book() {

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
                ", genre='" + genreType + '\'' +
                '}';
    }

    public String getGenreType() {
        return genreType;
    }

    public void setGenreType(String genreType) {
        this.genreType = genreType;
    }
}
