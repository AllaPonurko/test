package org.example.loader;

import org.example.entity.dialer.DialerGenre;
import org.example.entity.dialer.TypeItem;
import org.example.enums.GenreTypeEnum;
import org.example.enums.ProductTypeEnum;
import org.example.enums.interfaces.EnumEntity;
import org.example.repository.DialerGenreRepository;
import org.example.repository.TypeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private final DialerGenreRepository dialerGenreRepository;
    @Autowired
    private final TypeItemRepository typeItemRepository;

    public DataLoader(DialerGenreRepository dialerGenreRepository, TypeItemRepository typeItemRepository) {
        this.dialerGenreRepository = dialerGenreRepository;
        this.typeItemRepository = typeItemRepository;
    }
    private <T extends Enum<T>, E extends EnumEntity<T>> void loadEnumValues(T[] enumValues, Class<E> entityClass, JpaRepository<E, Long> repository) throws InvocationTargetException, NoSuchMethodException {
        if (repository.count() == 0) {
            for (T enumValue : enumValues) {
                E entity = createEntityInstance(enumValue,entityClass);
                repository.save(entity);
            }
        } else {
            List<T> existingEnumValues = repository.findAll().stream()
                    .map(EnumEntity::getEnumValue)
                    .collect(Collectors.toList());

            for (T enumValue : enumValues) {
                if (!existingEnumValues.contains(enumValue)) {
                    E entity = createEntityInstance(enumValue,entityClass);
                    repository.save(entity);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>, E extends EnumEntity<T>> E createEntityInstance(T enumValue, Class<E> entityClass) throws NoSuchMethodException, InvocationTargetException {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            entity.setEnumValue(enumValue);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate entity for enum " + enumValue, e);
        }
    }
    @Override
    public void run(String... args) throws Exception {
        loadEnumValues(GenreTypeEnum.values(),DialerGenre.class,dialerGenreRepository);
        loadEnumValues(ProductTypeEnum.values(), TypeItem.class,typeItemRepository);
    }
}
