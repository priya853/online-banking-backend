package com.onlinebanking.service;

import com.onlinebanking.dto.DashboardResponse;
import com.onlinebanking.dto.TransactionResponse;
import com.onlinebanking.entity.BankAccount;
import com.onlinebanking.entity.Transaction;
import com.onlinebanking.entity.User;
import com.onlinebanking.repository.BankAccountRepository;
import com.onlinebanking.repository.TransactionRepository;
import com.onlinebanking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public DashboardService(
            UserRepository userRepository,
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    public DashboardResponse getDashboard(String loggedInEmail) {

        // Find logged-in user
        User user = userRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Get user's accounts
        List<BankAccount> accounts =
                bankAccountRepository.findByUser(user);

        // Count accounts
        int totalAccounts = accounts.size();

        // Calculate total balance
        BigDecimal totalBalance = accounts.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get account numbers
        List<String> accountNumbers = accounts.stream()
                .map(BankAccount::getAccountNumber)
                .toList();

        // Get recent transactions
        List<Transaction> transactions;

        if (accountNumbers.isEmpty()) {
            transactions = List.of();
        } else {
            transactions = transactionRepository
                    .findByAccountNumberInOrderByTransactionDateDesc(
                            accountNumbers);
        }

        // Get latest 5 transactions
        List<TransactionResponse> recentTransactions =
                transactions.stream()
                        .limit(5)
                        .map(this::convertToResponse)
                        .toList();

        // Return dashboard response
        return new DashboardResponse(
                user.getName(),
                totalAccounts,
                totalBalance,
                recentTransactions
        );
    }

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