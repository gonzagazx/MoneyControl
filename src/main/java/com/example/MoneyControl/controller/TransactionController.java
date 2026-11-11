package com.example.MoneyControl.controller;

import com.example.MoneyControl.dto.TransactionRequest;
import com.example.MoneyControl.dto.TransactionResponse;
import com.example.MoneyControl.entity.User;
import com.example.MoneyControl.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request,
                                                      @AuthenticationPrincipal User user) {
        TransactionResponse response = transactionService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<TransactionResponse> findAll(@AuthenticationPrincipal User user) {
        return transactionService.findAll(user);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@PathVariable Long id,
                                      @Valid @RequestBody TransactionRequest request,
                                      @AuthenticationPrincipal User user) {
        return transactionService.update(id, request, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        transactionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}