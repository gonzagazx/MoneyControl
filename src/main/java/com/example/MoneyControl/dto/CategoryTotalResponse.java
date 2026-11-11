package com.example.MoneyControl.dto;

import java.math.BigDecimal;

public record CategoryTotalResponse(
        Long categoryId,
        String categoryName,
        BigDecimal total
) {
}