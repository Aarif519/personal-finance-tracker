package com.fintrack.fintrack.service;

import com.fintrack.fintrack.dto.BudgetRequest;
import com.fintrack.fintrack.dto.BudgetStatus;
import com.fintrack.fintrack.entity.Budget;
import com.fintrack.fintrack.entity.Category;
import com.fintrack.fintrack.entity.TransactionType;
import com.fintrack.fintrack.entity.User;
import com.fintrack.fintrack.repository.BudgetRepository;
import com.fintrack.fintrack.repository.CategoryRepository;
import com.fintrack.fintrack.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepo;
    private final CategoryRepository categoryRepo;
    private final TransactionRepository transactionRepo;

    public Budget setBudget(User user, BudgetRequest req) {
        Category category = categoryRepo.findById(req.getCategoryId()).orElseThrow();

        Budget budget = budgetRepo
                .findByUserIdAndCategoryIdAndMonthYear(user.getId(), category.getId(), req.getMonth())
                .orElseGet(() -> {
                    Budget b = new Budget();
                    b.setUser(user);
                    b.setCategory(category);
                    b.setMonthYear(req.getMonth());
                    return b;
                });

        budget.setMonthlyLimit(req.getMonthlyLimit());
        return budgetRepo.save(budget);
    }

    public List<BudgetStatus> getStatus(Long userId, String month) {
        YearMonth ym = YearMonth.parse(month);

        return budgetRepo.findByUserIdAndMonthYear(userId, month).stream().map(b -> {
            BigDecimal spent = transactionRepo.sumByCategory(
                    userId, b.getCategory().getId(), TransactionType.EXPENSE,
                    ym.atDay(1), ym.atEndOfMonth());
            BigDecimal remaining = b.getMonthlyLimit().subtract(spent);
            boolean exceeded = spent.compareTo(b.getMonthlyLimit()) > 0;

            return new BudgetStatus(b.getCategory().getName(), b.getMonthlyLimit(),
                    spent, remaining, exceeded);
        }).toList();
    }
}