package com.fintrack.fintrack.dto;

import com.fintrack.fintrack.entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    private Long categoryId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate transactionDate;
}