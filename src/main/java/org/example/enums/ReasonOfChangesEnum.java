package org.example.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReasonOfChangesEnum {
    MANUAL_DELETED("MANUAL_DELETED"),
    TIMEOUT_DELETED("TIMEOUT_DELETED"),
    CREATED_BY_USER("CREATED_BY_USER"),
    UPDATED_PAY("UPDATED_PAY"),
    CREATED_BY_ADMIN("CREATED_BY_ADMIN"),
    UPDATE_OF_DATA("UPDATE_OF_DATA");


    private String value;

    ReasonOfChangesEnum(String value) {
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
