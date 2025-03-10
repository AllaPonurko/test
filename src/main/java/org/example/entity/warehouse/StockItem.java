package org.example.entity.warehouse;

import jakarta.persistence.*;
import org.example.entity.product.Product;

import java.math.BigDecimal;

@Entity
@Table(name = "stock_items")
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalValue() {

        return (BigDecimal.valueOf(this.product.getPrice())).multiply(new BigDecimal(this.quantity));
    }

    public BigDecimal getNewTotalValue(double price, int quantity) {
        return (BigDecimal.valueOf(price).multiply(new BigDecimal(quantity)));
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
