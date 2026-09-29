package com.onlinebanking.controller;

import com.onlinebanking.dto.TransferRequest;
import com.onlinebanking.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(
            TransferService transferService) {

        this.transferService = transferService;
    }

    // Transfer money from sender account to receiver account
    @PostMapping
    public String transfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        transferService.transfer(
                request,
                loggedInEmail
        );

        return "Money transferred successfully";
    }
}