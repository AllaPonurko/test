package org.example.repositories;

import org.example.models.orders.OrderDetail;
import org.example.models.products.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    //List<Book> findAllBooksByIdUser(UUID userId);
}
