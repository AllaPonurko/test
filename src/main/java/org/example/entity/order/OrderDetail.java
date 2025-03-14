package org.example.entity.order;

import jakarta.persistence.*;
import org.example.entity.product.Book;
import org.example.entity.product.Product;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_details")
public class OrderDetail extends OrderBase{

@OneToMany(mappedBy = "orderDetail", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
private List<OrderProduct> orderProducts = new ArrayList<>();

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrderDetail(this);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        orderProducts.remove(orderProduct);
        orderProduct.setOrderDetail(null);
    }

}
