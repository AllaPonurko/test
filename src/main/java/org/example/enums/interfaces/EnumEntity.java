package org.example.enums.interfaces;

public interface EnumEntity<T extends Enum<T>> {
    T getEnumValue();
    void setEnumValue(T enumValue);
}
