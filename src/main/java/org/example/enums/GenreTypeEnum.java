package org.example.enums;

import com.fasterxml.jackson.annotation.JsonValue;


public enum GenreTypeEnum {
    DRAMA("DRAMA"),
    DETECTIVE("DETECTIVE"),
    NOVEL("NOVEL"),
    SHORT_STORY("SHORT STORY"),
    SCIENCE_FICTION_GENRE("SCIENCE FICTION GENRE"),
    ADVENTURES("ADVENTURES"),
    POETRY("POETRY");
    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    GenreTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}