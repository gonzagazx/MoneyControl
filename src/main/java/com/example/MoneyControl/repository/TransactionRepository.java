package com.example.MoneyControl.repository;

import com.example.MoneyControl.dto.CategoryTotalResponse;
import com.example.MoneyControl.entity.Transaction;
import com.example.MoneyControl.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategoryUserId(Long userId);

    Optional<Transaction> findByIdAndCategoryUserId(Long id, Long userId);

    boolean existsByIdAndCategoryUserId(Long id, Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.category.user.id = :userId
              AND t.category.type = :type
              AND t.date BETWEEN :start AND :end
            """)
    BigDecimal sumByTypeAndPeriod(@Param("userId") Long userId,
                                  @Param("type") TransactionType type,
                                  @Param("start") LocalDate start,
                                  @Param("end") LocalDate end);

    @Query("""
            SELECT new com.example.MoneyControl.dto.CategoryTotalResponse(c.id, c.name, SUM(t.amount))
            FROM Transaction t
            JOIN t.category c
            WHERE c.user.id = :userId
              AND c.type = :type
              AND t.date BETWEEN :start AND :end
            GROUP BY c.id, c.name
            ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryTotalResponse> totalsByCategory(@Param("userId") Long userId,
                                                 @Param("type") TransactionType type,
                                                 @Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);
}