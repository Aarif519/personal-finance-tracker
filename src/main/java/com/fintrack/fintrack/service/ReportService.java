package com.fintrack.fintrack.service;

import com.fintrack.fintrack.dto.CategoryTotal;
import com.fintrack.fintrack.dto.SummaryResponse;
import com.fintrack.fintrack.entity.TransactionType;
import com.fintrack.fintrack.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepo;

    public SummaryResponse getSummary(Long userId, String month) {
        YearMonth ym = YearMonth.parse(month);

        BigDecimal income = transactionRepo.sumByType(
                userId, TransactionType.INCOME, ym.atDay(1), ym.atEndOfMonth());
        BigDecimal expense = transactionRepo.sumByType(
                userId, TransactionType.EXPENSE, ym.atDay(1), ym.atEndOfMonth());

        return new SummaryResponse(income, expense, income.subtract(expense));
    }

    public List<CategoryTotal> getCategoryBreakdown(Long userId, String month) {
        YearMonth ym = YearMonth.parse(month);
        return transactionRepo.categoryTotals(
                userId, TransactionType.EXPENSE, ym.atDay(1), ym.atEndOfMonth());
    }
}