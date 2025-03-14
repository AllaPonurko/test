package org.example.enums;

public enum TypeOfActionWithStockItemEnum {
    RESTOCK("RESTOCK"),
    WITHDRAW("WITHDRAW"),
    ZERO_OF_INVENTORY("ZERO_OF_INVENTORY"),
    RESERVED("RESERVED");

    private String value;

    TypeOfActionWithStockItemEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
