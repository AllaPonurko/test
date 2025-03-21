package org.example.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductTypeEnum {
    BOOK("BOOK"),
    VENDOR("VENDOR"),
    ELECTRONIC("ELECTRONIC"),
    PRODUCT("PRODUCT");

    private String value;
    ProductTypeEnum(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
