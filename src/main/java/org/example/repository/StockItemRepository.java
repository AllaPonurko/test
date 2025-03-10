package org.example.repository;

import org.example.entity.warehouse.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem,Long> {
    @Query("select item from StockItem item where item.product.id=:productId and item.warehouse.locationNumber=:locationNumber")
    Optional<StockItem> findByProductAndWarehouse(@Param("productId") UUID productId,@Param("locationNumber") long locationNumber);
}
