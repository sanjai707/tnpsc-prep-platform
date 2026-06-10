package com.tnpsc.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tnpsc.app.entity.User;
import com.tnpsc.app.entity.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findAllByUser(User user);

    @Query("SELECT ua.question.topic, SUM(CASE WHEN ua.isCorrect = true THEN 1 ELSE 0 END), COUNT(ua) " +
           "FROM UserAnswer ua WHERE ua.user = :user GROUP BY ua.question.topic")
    List<Object[]> aggregateTopicPerformance(User user);

    @Query("SELECT ua.question.topic, MAX(ua.attemptedAt) FROM UserAnswer ua " +
           "WHERE ua.user = :user GROUP BY ua.question.topic")
    List<Object[]> findLastAttemptedPerTopic(User user);
}
