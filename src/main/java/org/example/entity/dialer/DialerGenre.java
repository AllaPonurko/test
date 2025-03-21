package org.example.entity.dialer;

import jakarta.persistence.*;
import org.example.converter.GenreTypeEnumConverter;
import org.example.enums.GenreTypeEnum;
import org.example.enums.interfaces.EnumEntity;

@Entity
@Table(name = "dialer_genre")
public class DialerGenre implements EnumEntity<GenreTypeEnum> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = GenreTypeEnumConverter.class)
    @Column(name = "genre_type_enum", nullable = false)
    private GenreTypeEnum genreTypeEnum;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public GenreTypeEnum getEnumValue() {
        return genreTypeEnum;
    }

    @Override
    public void setEnumValue(GenreTypeEnum genreTypeEnum) {
        this.genreTypeEnum = genreTypeEnum;
    }
}
