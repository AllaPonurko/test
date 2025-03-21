package org.example.repository;

import org.example.entity.log.LogOrderChanged;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogOrderChangedRepository extends JpaRepository<LogOrderChanged,Long> {
}
