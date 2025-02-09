package org.example.enums;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import org.example.enums.interfaces.CodeProvider;

public enum ProductType  {
    BOOK("BOOK"),
    VENDOR("VENDOR"),
    ELECTRONIC("ELECTRONIC");

    private String value;
    ProductType(String value) {
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
