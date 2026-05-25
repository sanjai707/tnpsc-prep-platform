package com.tnpsc.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnpsc.app.dto.AnswerResultDto;
import com.tnpsc.app.dto.AnswerSubmissionDto;
import com.tnpsc.app.dto.QuestionDto;
import com.tnpsc.app.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/daily")
public ResponseEntity<List<QuestionDto>> getDailyQuestions(
        @RequestParam(required = false) List<String> topics
) {
    return ResponseEntity.ok(questionService.findDailyQuestions(topics));
}

    @PostMapping("/submit")
    public ResponseEntity<AnswerResultDto> submitAnswer(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody AnswerSubmissionDto submission) {
        return ResponseEntity.ok(questionService.submitAnswer(userDetails.getUsername(), submission));
    }
}
