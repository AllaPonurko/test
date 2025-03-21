package org.example.repository;

import org.example.entity.order.OrderWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderWarehouseRepository extends JpaRepository<OrderWarehouse,Long> {

    List<OrderWarehouse> findAllByOrder_Id(UUID orderId);
}
