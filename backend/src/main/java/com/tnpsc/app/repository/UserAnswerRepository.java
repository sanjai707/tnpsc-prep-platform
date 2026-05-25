package com.tnpsc.app.repository;

import com.tnpsc.app.entity.UserAnswer;
import com.tnpsc.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findAllByUser(User user);

    @Query("SELECT ua.question.topic, SUM(CASE WHEN ua.isCorrect = true THEN 1 ELSE 0 END), COUNT(ua) " +
           "FROM UserAnswer ua WHERE ua.user = :user GROUP BY ua.question.topic")
    List<Object[]> aggregateTopicPerformance(User user);
}
