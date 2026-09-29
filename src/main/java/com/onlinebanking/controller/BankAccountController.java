package com.onlinebanking.controller;

import com.onlinebanking.dto.AccountResponse;
import com.onlinebanking.dto.DepositRequest;
import com.onlinebanking.dto.WithdrawRequest;
import com.onlinebanking.entity.BankAccount;
import com.onlinebanking.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(
            BankAccountService bankAccountService) {

        this.bankAccountService = bankAccountService;
    }

    // Create new bank account
    @PostMapping
    public AccountResponse createAccount(
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        BankAccount account =
                bankAccountService.createAccount(loggedInEmail);

        return convertToResponse(account);
    }

    // Get specific account
    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(
            @PathVariable String accountNumber,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        BankAccount account =
                bankAccountService.getAccountByNumber(
                        accountNumber,
                        loggedInEmail
                );

        return convertToResponse(account);
    }

    // Get all accounts of logged-in user
    @GetMapping("/my-accounts")
    public List<AccountResponse> getMyAccounts(
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        return bankAccountService
                .getAllAccounts(loggedInEmail)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Deposit money
    @PostMapping("/{accountNumber}/deposit")
    public AccountResponse deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        BankAccount account =
                bankAccountService.deposit(
                        accountNumber,
                        request.getAmount(),
                        loggedInEmail
                );

        return convertToResponse(account);
    }

    // Withdraw money
    @PostMapping("/{accountNumber}/withdraw")
    public AccountResponse withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawRequest request,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        BankAccount account =
                bankAccountService.withdraw(
                        accountNumber,
                        request.getAmount(),
                        loggedInEmail
                );

        return convertToResponse(account);
    }

    // Convert Entity to DTO
    private AccountResponse convertToResponse(
            BankAccount account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getUser().getId()
        );
    }
}