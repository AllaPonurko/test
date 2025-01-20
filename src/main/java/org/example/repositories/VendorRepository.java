package org.example.repositories;

import org.example.models.products.Vendor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID>{
   @NotNull
   Optional<Vendor>  findById(@NotNull UUID id);
   Optional<Vendor> findByBrand(String brand);
}
