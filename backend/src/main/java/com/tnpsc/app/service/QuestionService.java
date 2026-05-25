package com.tnpsc.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tnpsc.app.dto.AnswerResultDto;
import com.tnpsc.app.dto.AnswerSubmissionDto;
import com.tnpsc.app.dto.QuestionDto;
import com.tnpsc.app.entity.Question;
import com.tnpsc.app.entity.User;
import com.tnpsc.app.entity.UserAnswer;
import com.tnpsc.app.repository.QuestionRepository;
import com.tnpsc.app.repository.UserAnswerRepository;
import com.tnpsc.app.repository.UserRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;

    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository, UserAnswerRepository userAnswerRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public List<QuestionDto> findDailyQuestions(List<String> topics) {

    List<Question> questions;

    if (topics == null || topics.isEmpty() || topics.contains("Mixed Practice")) {
        questions = questionRepository.findAll();
    } else {
        questions = questionRepository.findQuestionsByTopics(topics);
    }

    return questions.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
}

    public AnswerResultDto submitAnswer(String email, AnswerSubmissionDto submission) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Question question = questionRepository.findById(submission.getQuestionId()).orElseThrow();
        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(submission.getSelectedAnswer());
        UserAnswer answer = new UserAnswer(user, question, submission.getSelectedAnswer(), isCorrect, LocalDateTime.now());
        userAnswerRepository.save(answer);
        updateStreak(user);

        AnswerResultDto result = new AnswerResultDto();
        result.setCorrect(isCorrect);
        result.setExplanationEn(question.getExplanationEn());
        result.setExplanationTa(question.getExplanationTa());
        result.setCorrectAnswer(question.getCorrectAnswer());
        return result;
    }

    private void updateStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastActive = user.getLastActiveDate();
        if (lastActive == null) {
            user.setStreakCount(1);
        } else if (lastActive.plusDays(1).equals(today) || lastActive.equals(today)) {
            user.setStreakCount(Math.max(user.getStreakCount(), 0) + (lastActive.equals(today) ? 0 : 1));
        } else {
            user.setStreakCount(1);
        }
        user.setLastActiveDate(today);
        userRepository.save(user);
    }

    private QuestionDto mapToDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setTopic(question.getTopic());
        dto.setQuestionEn(question.getQuestionEn());
        dto.setQuestionTa(question.getQuestionTa());
        dto.setOptionAEn(question.getOptionAEn());
        dto.setOptionATa(question.getOptionATa());
        dto.setOptionBEn(question.getOptionBEn());
        dto.setOptionBTa(question.getOptionBTa());
        dto.setOptionCEn(question.getOptionCEn());
        dto.setOptionCTa(question.getOptionCTa());
        dto.setOptionDEn(question.getOptionDEn());
        dto.setOptionDTa(question.getOptionDTa());
        return dto;
    }
}
