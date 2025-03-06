package org.example.entity.order;

import jakarta.persistence.*;
import org.example.entity.product.Book;
import org.example.entity.product.Product;

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

    private List<Product> itemList;
    public OrderDetail()
    {
        itemList=new ArrayList<Product>();
    }


    public List<Product> getItemList() {
        return itemList;
    }

    public void setItemList(List<Product> itemList) {
        this.itemList = itemList;
    }
}
