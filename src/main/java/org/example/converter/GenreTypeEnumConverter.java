package org.example.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.enums.GenreTypeEnum;

@Converter(autoApply = true)
public class GenreTypeEnumConverter implements AttributeConverter<GenreTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(GenreTypeEnum attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public GenreTypeEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? GenreTypeEnum.fromValue(dbData) : null;
    }
}