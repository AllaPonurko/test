package org.example.enums;

public enum LocationEnum {
    PRAHA_ZLICIN("PRAHA_ZLICIN"),
    PRAHA_VYSEGRAD("PRAHA_VYSEGRAD"),
    RUDNA("RUDNA"),
    PRUHONICE("PRUHONICE");
    private String value;

    LocationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
