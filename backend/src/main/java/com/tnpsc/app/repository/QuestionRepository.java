package com.tnpsc.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tnpsc.app.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
    SELECT * FROM questions
    WHERE (:topics IS NULL OR topic IN (:topics))
    ORDER BY random()
    LIMIT 10
    """, nativeQuery = true)
List<Question> findQuestionsByTopics(List<String> topics);
}
