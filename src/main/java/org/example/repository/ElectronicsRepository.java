package org.example.repository;

import org.example.entity.product.Electronic;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElectronicsRepository extends JpaRepository<Electronic, UUID> {

    List<Electronic> findByVendorId(UUID vendor_id);
    @NotNull
    Optional<Electronic> findById(@NotNull UUID id);
}
