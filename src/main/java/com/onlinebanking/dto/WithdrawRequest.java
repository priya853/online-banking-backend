package com.onlinebanking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WithdrawRequest {

    @NotNull(message = "Withdrawal amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Withdrawal amount must be greater than zero"
    )
    private BigDecimal amount;

    public WithdrawRequest() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}