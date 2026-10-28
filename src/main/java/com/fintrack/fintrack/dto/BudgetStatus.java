package com.fintrack.fintrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
public class BudgetStatus {
    private String category;
    private BigDecimal monthlyLimit;
    private BigDecimal spent;
    private BigDecimal remaining;
    private boolean exceeded;

    public int getPercent() {
        if (monthlyLimit.signum() == 0) {
            return 0;
        }
        return spent.multiply(BigDecimal.valueOf(100))
                .divide(monthlyLimit, 0, RoundingMode.HALF_UP)
                .intValue();
    }
} 