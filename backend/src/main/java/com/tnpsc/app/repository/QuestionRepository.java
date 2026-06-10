package com.tnpsc.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tnpsc.app.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM questions ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("limit") int limit);

    @Query(value = "SELECT * FROM questions WHERE topic IN (:topics) ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Question> findQuestionsByTopics(@Param("topics") List<String> topics, @Param("limit") int limit);

    @Query(value = "SELECT * FROM questions WHERE subject = :subject ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Question> findQuestionsBySubject(@Param("subject") String subject, @Param("limit") int limit);

    @Query(value = "SELECT * FROM questions WHERE subject = :subject AND id NOT IN (:attemptedIds) ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Question> findUnattemptedQuestionsBySubject(@Param("subject") String subject,
                                                    @Param("attemptedIds") List<Long> attemptedIds,
                                                    @Param("limit") int limit);

    @Query(value = "SELECT * FROM questions WHERE subject = :subject AND id IN (:attemptedIds) ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Question> findAttemptedQuestionsBySubject(@Param("subject") String subject,
                                                  @Param("attemptedIds") List<Long> attemptedIds,
                                                  @Param("limit") int limit);

    @Query(value = "SELECT * FROM questions WHERE id NOT IN (:excludeIds) ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Question> findQuestionsExcludingIds(@Param("excludeIds") List<Long> excludeIds, @Param("limit") int limit);
}
