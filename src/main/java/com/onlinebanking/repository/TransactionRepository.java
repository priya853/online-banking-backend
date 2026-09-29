package com.onlinebanking.repository;

import com.onlinebanking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    // Get transactions of one account
    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(
            String accountNumber
    );

    // Get transactions of multiple accounts
    List<Transaction> findByAccountNumberInOrderByTransactionDateDesc(
            List<String> accountNumbers
    );
}