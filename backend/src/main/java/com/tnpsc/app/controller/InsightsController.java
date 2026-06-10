package com.tnpsc.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<DailyInsightDto>> getTodayInsights() {
        List<DailyInsightDto> list = questionService.getDailyInsights();
        return ResponseEntity.ok(list);
    }
}
