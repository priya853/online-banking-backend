package com.onlinebanking.service;

import com.onlinebanking.entity.BankAccount;
import com.onlinebanking.entity.Transaction;
import com.onlinebanking.entity.User;
import com.onlinebanking.repository.BankAccountRepository;
import com.onlinebanking.repository.TransactionRepository;
import com.onlinebanking.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    // Get transactions of one account
    public List<Transaction> getTransactionsByAccountNumber(
            String accountNumber,
            String loggedInEmail) {

        BankAccount account = bankAccountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Bank account not found"));

        User loggedInUser = userRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!account.getUser().getId()
                .equals(loggedInUser.getId())) {

            throw new AccessDeniedException(
                    "You are not authorized to access this transaction history");
        }

        return transactionRepository
                .findByAccountNumberOrderByTransactionDateDesc(
                        accountNumber);
    }

    // Get transactions of all accounts of logged-in user
    public List<Transaction> getMyTransactions(
            String loggedInEmail) {

        User loggedInUser = userRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<BankAccount> accounts =
                bankAccountRepository
                        .findByUser(loggedInUser);

        List<String> accountNumbers =
                accounts.stream()
                        .map(BankAccount::getAccountNumber)
                        .toList();

        if (accountNumbers.isEmpty()) {
            return List.of();
        }

        return transactionRepository
                .findByAccountNumberInOrderByTransactionDateDesc(
                        accountNumbers);
    }
}