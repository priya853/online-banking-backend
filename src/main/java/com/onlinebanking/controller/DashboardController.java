package com.onlinebanking.controller;

import com.onlinebanking.dto.DashboardResponse;
import com.onlinebanking.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboard(
            Authentication authentication) {

        String loggedInEmail = authentication.getName();

        return dashboardService.getDashboard(
                loggedInEmail);
    }
}