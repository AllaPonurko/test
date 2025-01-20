package org.example.services.interfaces;

import java.util.List;
import java.util.Optional;

public interface IVendorService <T>{
    Optional<T> findByBrand(String brand);
}
