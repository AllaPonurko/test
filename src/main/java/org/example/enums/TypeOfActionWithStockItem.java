package org.example.enums;

public enum TypeOfActionWithStockItem {
    RESTOCK("RESTOCK"),
    WITHDRAW("WITHDRAW"),
    ZERO_OF_INVENTORY("ZERO_OF_INVENTORY");
    private String value;

    TypeOfActionWithStockItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
