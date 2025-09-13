package com.example.testApp.repository;


import com.example.testApp.entity.OutboxTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxTable,Integer> {

    @Query("SELECT ot FROM OutboxTable ot WHERE ot.isProcessed= :isProcessed ORDER BY ot.createdAt ASC")
    List<OutboxTable> get50records(@Param("isProcessed") boolean isProcessed, Pageable pageable);

}
