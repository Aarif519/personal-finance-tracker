package com.fintrack.fintrack.repository;

import com.fintrack.fintrack.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserIdAndCategoryIdAndMonthYear(Long userId, Long categoryId, String monthYear);

    List<Budget> findByUserIdAndMonthYear(Long userId, String monthYear);
}