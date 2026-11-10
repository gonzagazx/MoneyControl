package com.example.MoneyControl.dto;

import com.example.MoneyControl.entity.Transaction;
import com.example.MoneyControl.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        String description,
        BigDecimal amount,
        LocalDate date,
        Long categoryId,
        String categoryName,
        TransactionType type
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getCategory().getType()
        );
    }
}