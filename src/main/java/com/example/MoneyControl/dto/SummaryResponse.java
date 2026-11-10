package com.example.MoneyControl.dto;

import java.math.BigDecimal;

public record SummaryResponse(
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balance
) {
}