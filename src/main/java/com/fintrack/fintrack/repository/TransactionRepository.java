package com.fintrack.fintrack.repository;

import com.fintrack.fintrack.dto.CategoryTotal;
import com.fintrack.fintrack.entity.Transaction;
import com.fintrack.fintrack.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.id = :userId AND t.type = :type " +
           "AND t.transactionDate BETWEEN :start AND :end")
    BigDecimal sumByType(@Param("userId") Long userId,
                         @Param("type") TransactionType type,
                         @Param("start") LocalDate start,
                         @Param("end") LocalDate end);

    @Query("SELECT new com.fintrack.fintrack.dto.CategoryTotal(c.name, SUM(t.amount)) " +
           "FROM Transaction t JOIN t.category c " +
           "WHERE t.user.id = :userId AND t.type = :type " +
           "AND t.transactionDate BETWEEN :start AND :end " +
           "GROUP BY c.name ORDER BY SUM(t.amount) DESC")
    List<CategoryTotal> categoryTotals(@Param("userId") Long userId,
                                       @Param("type") TransactionType type,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.id = :userId AND t.category.id = :categoryId AND t.type = :type " +
           "AND t.transactionDate BETWEEN :start AND :end")
    BigDecimal sumByCategory(@Param("userId") Long userId,
                             @Param("categoryId") Long categoryId,
                             @Param("type") TransactionType type,
                             @Param("start") LocalDate start,
                             @Param("end") LocalDate end);
}