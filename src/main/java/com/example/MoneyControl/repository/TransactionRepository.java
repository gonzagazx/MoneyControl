package com.example.MoneyControl.repository;

import com.example.MoneyControl.entity.Transaction;
import com.example.MoneyControl.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.category.type = :type
              AND t.date BETWEEN :start AND :end
            """)
    BigDecimal sumByTypeAndPeriod(@Param("type") TransactionType type,
                                  @Param("start") LocalDate start,
                                  @Param("end") LocalDate end);
}