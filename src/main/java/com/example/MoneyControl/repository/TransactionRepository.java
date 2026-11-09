package com.example.MoneyControl.repository;

import com.example.MoneyControl.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}