package com.onlinebanking.service;

import com.onlinebanking.dto.TransferRequest;
import com.onlinebanking.entity.BankAccount;
import com.onlinebanking.entity.Transaction;
import com.onlinebanking.entity.User;
import com.onlinebanking.repository.BankAccountRepository;
import com.onlinebanking.repository.TransactionRepository;
import com.onlinebanking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransferService(
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository) {

        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void transfer(
            TransferRequest request,
            String loggedInEmail) {

        // Validate transfer amount
        validateAmount(request.getAmount());

        // Sender and receiver cannot be same
        if (request.getSenderAccountNumber()
                .equals(request.getReceiverAccountNumber())) {

            throw new RuntimeException(
                    "Sender and receiver accounts cannot be same"
            );
        }

        // Find logged-in user
        User loggedInUser =
                userRepository
                        .findByEmail(loggedInEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                ));

        // Find sender account
        BankAccount senderAccount =
                bankAccountRepository
                        .findByAccountNumber(
                                request.getSenderAccountNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sender account not found"
                                ));

        // Check sender account ownership
        if (!senderAccount.getUser().getId()
                .equals(loggedInUser.getId())) {

            throw new AccessDeniedException(
                    "You are not authorized to transfer from this account"
            );
        }

        // Find receiver account
        BankAccount receiverAccount =
                bankAccountRepository
                        .findByAccountNumber(
                                request.getReceiverAccountNumber()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Receiver account not found"
                                ));

        // Check sufficient balance
        if (senderAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException(
                    "Insufficient balance"
            );
        }

        // Calculate new balances
        BigDecimal senderNewBalance =
                senderAccount.getBalance()
                        .subtract(request.getAmount());

        BigDecimal receiverNewBalance =
                receiverAccount.getBalance()
                        .add(request.getAmount());

        // Update balances
        senderAccount.setBalance(senderNewBalance);
        receiverAccount.setBalance(receiverNewBalance);

        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(receiverAccount);

        // Save sender transaction
        saveTransaction(
                senderAccount.getAccountNumber(),
                "TRANSFER_SENT",
                request.getAmount(),
                senderNewBalance
        );

        // Save receiver transaction
        saveTransaction(
                receiverAccount.getAccountNumber(),
                "TRANSFER_RECEIVED",
                request.getAmount(),
                receiverNewBalance
        );
    }

    // Validate amount
    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Transfer amount must be greater than zero"
            );
        }
    }

    // Save transaction
    private void saveTransaction(
            String accountNumber,
            String type,
            BigDecimal amount,
            BigDecimal balanceAfterTransaction) {

        Transaction transaction =
                new Transaction();

        transaction.setAccountNumber(
                accountNumber
        );

        transaction.setType(type);

        transaction.setAmount(amount);

        transaction.setBalanceAfterTransaction(
                balanceAfterTransaction
        );

        transaction.setTransactionDate(
                LocalDateTime.now()
        );

        transactionRepository.save(transaction);
    }
}