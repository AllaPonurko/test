package org.example.entity.order;

import jakarta.persistence.*;
import org.example.entity.product.Book;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_details")
public class OrderDetail extends OrderBase{
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    private List<Book> booksList;
    public OrderDetail()
    {
        booksList=new ArrayList<Book>();
    }
    public List<Book> getbooksList() {

        return booksList;
    }
    public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
    }
}
