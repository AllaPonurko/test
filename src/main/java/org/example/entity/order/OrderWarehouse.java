package org.example.entity.order;

import jakarta.persistence.*;
import org.example.entity.product.Product;
import org.example.entity.warehouse.Warehouse;
import org.example.enums.OrderStatusEnum;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class OrderWarehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Order order;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private Product product;

    private long quantity;

    public OrderWarehouse(Product product, Warehouse warehouse, Order order, long quantity) {
        this.warehouse = warehouse;
        this.product = product;
        this.order = order;
        this.quantity = quantity;
    }

    public OrderWarehouse() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
