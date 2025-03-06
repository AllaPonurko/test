package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderDTO {
    private UUID id;
    private String description;
    private LocalDateTime createAt;
    private boolean isPayed;
    private boolean isValid;
    private double totalPrice;
    private UUID userId;
    private UUID orderDetailId;

    public OrderDTO(UUID id, String description, LocalDateTime createAt, boolean isPayed, boolean isValid, double totalPrice, UUID userId, UUID orderDetailId) {
        this.id = id;
        this.description = description;
        this.createAt = createAt;
        this.isPayed = isPayed;
        this.isValid = isValid;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderDetailId = orderDetailId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(UUID orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
}
