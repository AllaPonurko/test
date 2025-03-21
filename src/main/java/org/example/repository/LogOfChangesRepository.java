package org.example.repository;

import org.example.entity.log.LogItemChanged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogOfChangesRepository extends JpaRepository<LogItemChanged, Long> {
}
