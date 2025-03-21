package org.example.entity.dialer;

import jakarta.persistence.*;
import org.example.enums.ProductTypeEnum;
import org.example.enums.interfaces.EnumEntity;

@Entity
@Table(name = "type_item")
public class TypeItem implements EnumEntity<ProductTypeEnum> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_enum", nullable = false)
    private ProductTypeEnum typeEnum;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    @Override
    public ProductTypeEnum getEnumValue() {
        return typeEnum;
    }

    @Override
    public void setEnumValue(ProductTypeEnum enumValue) {
        this.typeEnum = enumValue;
    }
}
