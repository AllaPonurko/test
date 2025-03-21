package org.example.enums;

public enum EmailSendStatusEnum {
    START("START"),
    SENT("SENT"),
    FAILED("FAILED"),
    REMOVED("REMOVED")
    ;
    private String value;

    EmailSendStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
