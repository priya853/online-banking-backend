package com.onlinebanking.controller;

import com.onlinebanking.dto.AdminAccountResponse;
import com.onlinebanking.dto.AdminTransactionResponse;
import com.onlinebanking.dto.AdminUserResponse;
import com.onlinebanking.entity.BankAccount;
import com.onlinebanking.entity.Transaction;
import com.onlinebanking.entity.User;
import com.onlinebanking.repository.BankAccountRepository;
import com.onlinebanking.repository.TransactionRepository;
import com.onlinebanking.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public AdminController(
            UserRepository userRepository,
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    // GET ALL USERS

    @GetMapping("/users")
    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertUserToResponse)
                .toList();
    }

    // BLOCK USER

    @PutMapping("/users/{id}/block")
    public String blockUser(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setStatus("BLOCKED");

        userRepository.save(user);

        return "User blocked successfully";
    }


    // ACTIVATE USER

    @PutMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setStatus("ACTIVE");

        userRepository.save(user);

        return "User activated successfully";
    }

    // GET ALL ACCOUNTS

    @GetMapping("/accounts")
    public List<AdminAccountResponse> getAllAccounts() {

        return bankAccountRepository.findAll()
                .stream()
                .map(this::convertAccountToResponse)
                .toList();
    }


    // GET ALL TRANSACTIONS

    @GetMapping("/transactions")
    public List<AdminTransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::convertTransactionToResponse)
                .toList();
    }


    // CONVERT USER TO RESPONSE

    private AdminUserResponse convertUserToResponse(User user) {

        return new AdminUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }


    // CONVERT ACCOUNT TO RESPONSE

    private AdminAccountResponse convertAccountToResponse(
            BankAccount account) {

        User user = account.getUser();

        return new AdminAccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }


    // CONVERT TRANSACTION TO RESPONSE

    private AdminTransactionResponse convertTransactionToResponse(
            Transaction transaction) {

        return new AdminTransactionResponse(
                transaction.getId(),
                transaction.getAccountNumber(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction(),
                transaction.getTransactionDate()
        );
    }
}