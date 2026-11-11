package com.example.MoneyControl.service;

import com.example.MoneyControl.dto.TransactionRequest;
import com.example.MoneyControl.dto.TransactionResponse;
import com.example.MoneyControl.entity.Category;
import com.example.MoneyControl.entity.Transaction;
import com.example.MoneyControl.entity.User;
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
    public TransactionResponse create(TransactionRequest request, User user) {
        Category category = findUserCategory(request.categoryId(), user);

        Transaction transaction = new Transaction();
        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setCategory(category);

        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> findAll(User user) {
        return transactionRepository.findByCategoryUserId(user.getId())
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request, User user) {
        Transaction transaction = transactionRepository.findByIdAndCategoryUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));

        Category category = findUserCategory(request.categoryId(), user);

        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setCategory(category);

        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    public void delete(Long id, User user) {
        if (!transactionRepository.existsByIdAndCategoryUserId(id, user.getId())) {
            throw new ResourceNotFoundException("Transação não encontrada: " + id);
        }
        transactionRepository.deleteById(id);
    }

    private Category findUserCategory(Long categoryId, User user) {
        return categoryRepository.findByIdAndUserId(categoryId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + categoryId));
    }
}