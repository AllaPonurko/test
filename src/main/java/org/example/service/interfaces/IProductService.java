package org.example.service.interfaces;

import java.io.IOException;

import java.util.Optional;

public interface IProductService<T,DTO> {
    Optional<T> findById(String id);

    T createItem(DTO baseDTO) throws IOException, ClassNotFoundException, RuntimeException;

}
