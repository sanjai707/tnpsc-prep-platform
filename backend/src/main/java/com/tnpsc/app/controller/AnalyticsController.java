package com.tnpsc.app.controller;

import com.tnpsc.app.dto.StatSummaryDto;
import com.tnpsc.app.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<StatSummaryDto> getStats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(analyticsService.buildStats(userDetails.getUsername()));
    }
}
