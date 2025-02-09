package org.example.repository;

import org.example.entity.order.OrderDetail;
import org.example.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    //List<Book> findAllBooksByIdUser(UUID userId);
    @Query(value = "select orderDetail from OrderDetail orderDetail where orderDetail.id=:uuid")
    OrderDetail findByUuid(UUID uuid);
}
