package com.fintrack.fintrack.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequest {
    private Long categoryId;
    private BigDecimal monthlyLimit;
    private String month;   // "2026-10"
}