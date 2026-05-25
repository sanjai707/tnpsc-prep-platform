package com.tnpsc.app.service;

import com.tnpsc.app.dto.StatSummaryDto;
import com.tnpsc.app.entity.User;
import com.tnpsc.app.repository.UserAnswerRepository;
import com.tnpsc.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;

    public AnalyticsService(UserAnswerRepository userAnswerRepository, UserRepository userRepository) {
        this.userAnswerRepository = userAnswerRepository;
        this.userRepository = userRepository;
    }

    public StatSummaryDto buildStats(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<com.tnpsc.app.entity.UserAnswer> answers = userAnswerRepository.findAllByUser(user);
        long total = answers.size();
        long correct = answers.stream().filter(a -> Boolean.TRUE.equals(a.getIsCorrect())).count();
        double accuracy = total == 0 ? 0 : (correct * 100.0 / total);

        StatSummaryDto summary = new StatSummaryDto();
        summary.setTotalAttempts(total);
        summary.setCorrectAttempts(correct);
        summary.setAccuracy(Math.round(accuracy * 100.0) / 100.0);
        summary.setStreakCount(user.getStreakCount());

        List<StatSummaryDto.WeakTopic> weakTopics = new ArrayList<>();
        for (Object[] row : userAnswerRepository.aggregateTopicPerformance(user)) {
            String topic = (String) row[0];
            Long topicCorrect = (Long) row[1];
            Long topicTotal = (Long) row[2];
            double topicAccuracy = topicTotal == 0 ? 0 : topicCorrect * 100.0 / topicTotal;
            if (topicAccuracy < 60) {
                weakTopics.add(new StatSummaryDto.WeakTopic(topic, topicCorrect, topicTotal));
            }
        }
        summary.setWeakTopics(weakTopics);
        return summary;
    }
}
