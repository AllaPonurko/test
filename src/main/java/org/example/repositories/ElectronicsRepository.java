package org.example.repositories;

import org.example.models.products.Electronics;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElectronicsRepository extends JpaRepository<Electronics, UUID> {

    List<Electronics> findByVendorId(UUID vendor_id);
    @NotNull
    Optional<Electronics> findById(@NotNull UUID id);
}
