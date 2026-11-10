package com.example.MoneyControl.service;

import com.example.MoneyControl.dto.TransactionRequest;
import com.example.MoneyControl.dto.TransactionResponse;
import com.example.MoneyControl.entity.Category;
import com.example.MoneyControl.entity.Transaction;
import com.example.MoneyControl.exception.ResourceNotFoundException;
import com.example.MoneyControl.repository.CategoryRepository;
import com.example.MoneyControl.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: " + request.categoryId()));

        Transaction transaction = new Transaction();
        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setCategory(category);

        Transaction saved = transactionRepository.save(transaction);
        return TransactionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }
}