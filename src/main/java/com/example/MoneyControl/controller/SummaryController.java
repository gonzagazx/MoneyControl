package com.example.MoneyControl.controller;

import com.example.MoneyControl.dto.CategoryTotalResponse;
import com.example.MoneyControl.dto.SummaryResponse;
import com.example.MoneyControl.entity.TransactionType;
import com.example.MoneyControl.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    public SummaryResponse getSummary(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        YearMonth target = (month != null) ? month : YearMonth.now();
        return summaryService.getMonthlySummary(target);
    }

    @GetMapping("/by-category")
    public List<CategoryTotalResponse> getByCategory(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            @RequestParam(defaultValue = "EXPENSE") TransactionType type) {

        YearMonth target = (month != null) ? month : YearMonth.now();
        return summaryService.getTotalsByCategory(target, type);
    }
}