package org.example.service.interfaces;

import java.util.Optional;

public interface IVendorService <T>{
    Optional<T> findByBrand(String brand);
}
