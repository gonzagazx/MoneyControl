package com.example.MoneyControl.service;

import com.example.MoneyControl.dto.CategoryTotalResponse;
import com.example.MoneyControl.dto.SummaryResponse;
import com.example.MoneyControl.entity.TransactionType;
import com.example.MoneyControl.entity.User;
import com.example.MoneyControl.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public SummaryResponse getMonthlySummary(YearMonth month, User user) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        BigDecimal income = transactionRepository.sumByTypeAndPeriod(
                user.getId(), TransactionType.INCOME, start, end);
        BigDecimal expense = transactionRepository.sumByTypeAndPeriod(
                user.getId(), TransactionType.EXPENSE, start, end);

        return new SummaryResponse(income, expense, income.subtract(expense));
    }

    @Transactional(readOnly = true)
    public List<CategoryTotalResponse> getTotalsByCategory(YearMonth month, TransactionType type, User user) {
        return transactionRepository.totalsByCategory(
                user.getId(), type, month.atDay(1), month.atEndOfMonth());
    }
}