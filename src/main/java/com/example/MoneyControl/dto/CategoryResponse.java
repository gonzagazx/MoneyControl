package com.example.MoneyControl.dto;

import com.example.MoneyControl.entity.Category;
import com.example.MoneyControl.entity.TransactionType;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType type
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }
}