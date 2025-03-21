package org.example.entity.log;

import jakarta.persistence.*;
import org.example.entity.order.Order;
import org.example.enums.OrderStatusEnum;
import org.example.enums.TypeOfChangesEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class LogOrderChanged {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Order order;
    @CreationTimestamp
    private LocalDateTime timeOfCreated;
    @UpdateTimestamp
    private LocalDateTime timeOfChanges;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
    @Enumerated(EnumType.STRING)
    private TypeOfChangesEnum typeOfChanges;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getTimeOfCreated() {
        return timeOfCreated;
    }

    public void setTimeOfCreated(LocalDateTime timeOfCreated) {
        this.timeOfCreated = timeOfCreated;
    }

    public LocalDateTime getTimeOfChanges() {
        return timeOfChanges;
    }

    public void setTimeOfChanges(LocalDateTime timeOfChanges) {
        this.timeOfChanges = timeOfChanges;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public TypeOfChangesEnum getTypeOfChanges() {
        return typeOfChanges;
    }

    public void setTypeOfChanges(TypeOfChangesEnum typeOfChanges) {
        this.typeOfChanges = typeOfChanges;
    }
}
