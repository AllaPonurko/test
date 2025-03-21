package org.example.enums;

public enum EmailSendTypeEnum {
    AUTO_BY_EVENT("AUTO_BY_EVENT"),
    MANUAL("MANUAL")
    ;
    private String value;

    EmailSendTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
