package org.example.repositories;

import org.example.models.orders.Order;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>  {
   @NotNull
   Optional<Order> findById(@NotNull UUID uuid);

   Optional<List<Order>> findAllByUserId(@NotNull UUID user_id) ;
}
