package org.example.enums;

public enum OrderStatusEnum {
    PENDING("PENDING"), // Очікується
    COMPLETED("COMPLETED"), // Виконано
    CANCELLED("CANCELLED"), // Скасовано
    PAYED ("PAYED")// Відправлено
    ;
    private String value;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
