package com.tnpsc.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnpsc.app.dto.DailyInsightDto;
import com.tnpsc.app.service.QuestionService;

@RestController
@RequestMapping("/insights")
public class InsightsController {

    private final QuestionService questionService;

    public InsightsController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/today")
    public ResponseEntity<List<DailyInsightDto>> getTodayInsights(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("===== DEBUG INSIGHTS =====");
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: UserDetails = " + userDetails);
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: Username = " + (userDetails != null ? userDetails.getUsername() : "NULL"));
        String userEmail = userDetails != null ? userDetails.getUsername() : null;
        List<DailyInsightDto> list = questionService.getDailyInsights(userEmail);
        return ResponseEntity.ok(list);
    }
}
