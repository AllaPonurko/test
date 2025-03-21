package org.example.repository;

import org.example.entity.log.LogStockItemChanged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LogStockItemChangedRepository extends JpaRepository <LogStockItemChanged,Long>{
}
