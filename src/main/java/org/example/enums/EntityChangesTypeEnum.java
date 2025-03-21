package org.example.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EntityChangesTypeEnum {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private String value;
    EntityChangesTypeEnum(String value) {
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
