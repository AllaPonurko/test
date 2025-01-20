package org.example.services.interfaces;

import org.example.dto.BaseDTO;
import org.example.dto.OrderDTO;
import org.example.models.orders.Order;

import java.io.IOException;

import java.util.Optional;

public interface IProductService<T,DTO> {
    Optional<T> findById(String id);

    T createProduct(DTO baseDTO) throws IOException, ClassNotFoundException, RuntimeException;

}
