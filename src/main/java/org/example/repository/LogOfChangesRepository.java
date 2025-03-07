package org.example.repository;

import org.example.entity.log.LogOfChanges;
import org.example.entity.product.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogOfChangesRepository extends JpaRepository<LogOfChanges, Long> {
}
