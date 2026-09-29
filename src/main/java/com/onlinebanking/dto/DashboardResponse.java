package com.onlinebanking.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {

    private String userName;
    private int totalAccounts;
    private BigDecimal totalBalance;
    private List<TransactionResponse> recentTransactions;

    public DashboardResponse() {
    }

    public DashboardResponse(
            String userName,
            int totalAccounts,
            BigDecimal totalBalance,
            List<TransactionResponse> recentTransactions) {

        this.userName = userName;
        this.totalAccounts = totalAccounts;
        this.totalBalance = totalBalance;
        this.recentTransactions = recentTransactions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(int totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public List<TransactionResponse> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(
            List<TransactionResponse> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
}