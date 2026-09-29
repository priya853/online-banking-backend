package com.onlinebanking.dto;

import java.math.BigDecimal;

public class AdminAccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Long userId;
    private String userName;
    private String userEmail;

    public AdminAccountResponse() {
    }

    public AdminAccountResponse(
            Long id,
            String accountNumber,
            BigDecimal balance,
            Long userId,
            String userName,
            String userEmail) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}