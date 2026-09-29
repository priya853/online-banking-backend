package com.onlinebanking.controller;

import com.onlinebanking.dto.TransactionResponse;
import com.onlinebanking.entity.Transaction;
import com.onlinebanking.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    // Get transaction history of a specific account
    @GetMapping("/account/{accountNumber}")
    public List<TransactionResponse> getTransactionHistory(
            @PathVariable String accountNumber,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        List<Transaction> transactions =
                transactionService.getTransactionsByAccountNumber(
                        accountNumber,
                        loggedInEmail
                );

        return transactions.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get transactions of all accounts belonging to logged-in user
    @GetMapping("/my-transactions")
    public List<TransactionResponse> getMyTransactions(
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        List<Transaction> transactions =
                transactionService.getMyTransactions(
                        loggedInEmail
                );

        return transactions.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Convert Entity to DTO
    private TransactionResponse convertToResponse(
            Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountNumber(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction(),
                transaction.getTransactionDate()
        );
    }
}