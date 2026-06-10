package com.tnpsc.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tnpsc.app.dto.AnswerResultDto;
import com.tnpsc.app.dto.AnswerSubmissionDto;
import com.tnpsc.app.dto.DailyInsightDto;
import com.tnpsc.app.dto.QuestionDto;
import com.tnpsc.app.entity.Question;
import com.tnpsc.app.entity.User;
import com.tnpsc.app.entity.UserAnswer;
import com.tnpsc.app.repository.QuestionRepository;
import com.tnpsc.app.repository.UserAnswerRepository;
import com.tnpsc.app.repository.UserRepository;
import com.tnpsc.app.util.SubjectMapper;

@Service
public class QuestionService {

    private static final Map<String, Integer> SUBJECT_WEIGHTS = Map.of(
            "History", 15,
            "Geography", 15,
            "Polity", 15,
            "Economics", 15,
            "Science", 15,
            "Current Affairs", 15,
            "Aptitude", 10
    );

    private static final Map<String, Integer> EXAM_IMPORTANCE_MAP = Map.of(
            "Polity", 10,
            "Current Affairs", 10,
            "History", 8,
            "Geography", 7,
            "Science", 7,
            "Economics", 6,
            "General", 5
    );

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;

    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository, UserAnswerRepository userAnswerRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public List<DailyInsightDto> getDailyInsights() {
        return getDailyInsights(null);
    }

    public List<DailyInsightDto> getDailyInsights(String userEmail) {
        System.out.println("===== DEBUG SERVICE =====");
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: getDailyInsights called");
        System.out.println("DEBUG ONLY - REMOVE AFTER INVESTIGATION: userEmail = " + userEmail);
        System.out.println("================================");
        System.out.println("INSIGHTS CALLED");
        System.out.println("USER EMAIL = " + userEmail);
        System.out.println("================================");

        if (userEmail == null || userEmail.isBlank()) {
            return getFallbackDailyInsights();
        }

        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return getFallbackDailyInsights();
        }

        Map<String, TopicPerformance> performanceByTopic = buildPerformanceMap(user);
        Map<String, LocalDateTime> lastAttemptByTopic = buildLastAttemptMap(user);
        List<TopicScore> topicScores = SubjectMapper.getAllTopics().stream()
                .map(topic -> buildTopicScore(topic, performanceByTopic, lastAttemptByTopic))
                .sorted(Comparator.comparingDouble(TopicScore::getPriorityScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<DailyInsightDto> insights = new ArrayList<>();
        for (TopicScore score : topicScores) {
            List<Question> questions = questionRepository.findQuestionsByTopics(List.of(score.getTopic()), 1);
            if (questions == null || questions.isEmpty()) {
                continue;
            }
            Question question = questions.get(0);
            DailyInsightDto dto = new DailyInsightDto();
            dto.setTopic(question.getTopic());
            dto.setTitle(question.getQuestionEn() != null ? question.getQuestionEn() : "");
            dto.setExplanation(question.getExplanationEn());
            dto.setTnpscTip(null);
            dto.setMiniQuiz(question.getQuestionEn());
            dto.setPriorityScore(score.getPriorityScore());
            dto.setWeaknessLevel(computeWeaknessLevel(score.getWeaknessScore()));
            System.out.println(
    "DEBUG -> " +
    score.getTopic() +
    " priority=" +
    score.getPriorityScore() +
    " weakness=" +
    computeWeaknessLevel(score.getWeaknessScore())
);
            insights.add(dto);
        }

        if (insights.isEmpty()) {
            return getFallbackDailyInsights();
        }
        return insights;
    }

    private List<DailyInsightDto> getFallbackDailyInsights() {
        int count = new java.util.Random().nextInt(3) + 3; // 3,4,5
        List<Question> questions = questionRepository.findRandomQuestions(count);
        if (questions == null || questions.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return questions.stream().limit(count).map(q -> {
            DailyInsightDto dto = new DailyInsightDto();
            dto.setTopic(q.getTopic());
            String title = q.getQuestionEn();
            if (title == null || title.isBlank()) {
                String expl = q.getExplanationEn();
                if (expl != null && !expl.isBlank()) {
                    String[] parts = expl.split("\\.");
                    title = parts.length > 0 ? parts[0] : expl;
                } else {
                    title = "";
                }
            }
            dto.setTitle(title);
            dto.setExplanation(q.getExplanationEn());
            dto.setTnpscTip(null);
            dto.setMiniQuiz(q.getQuestionEn());
            return dto;
        }).collect(Collectors.toList());
    }

    private Map<String, TopicPerformance> buildPerformanceMap(User user) {
        Map<String, TopicPerformance> performanceMap = new LinkedHashMap<>();
        for (Object[] row : userAnswerRepository.aggregateTopicPerformance(user)) {
            if (row == null || row.length < 3) {
                continue;
            }
            String topic = (String) row[0];
            Number correctCount = (Number) row[1];
            Number totalCount = (Number) row[2];
            if (topic == null || totalCount == null || totalCount.intValue() == 0) {
                continue;
            }
            double accuracy = correctCount == null ? 0.0 : (correctCount.doubleValue() * 100.0 / totalCount.doubleValue());
            performanceMap.put(topic, new TopicPerformance(accuracy, totalCount.intValue()));
        }
        return performanceMap;
    }

    private Map<String, LocalDateTime> buildLastAttemptMap(User user) {
        Map<String, LocalDateTime> lastAttemptMap = new LinkedHashMap<>();
        for (Object[] row : userAnswerRepository.findLastAttemptedPerTopic(user)) {
            if (row == null || row.length < 2) {
                continue;
            }
            String topic = (String) row[0];
            LocalDateTime attemptedAt = (LocalDateTime) row[1];
            if (topic != null && attemptedAt != null) {
                lastAttemptMap.put(topic, attemptedAt);
            }
        }
        return lastAttemptMap;
    }

    private TopicScore buildTopicScore(String topic, Map<String, TopicPerformance> performanceByTopic, Map<String, LocalDateTime> lastAttemptByTopic) {
        TopicPerformance performance = performanceByTopic.get(topic);
        boolean attempted = performance != null;
        double weaknessScore = attempted ? (100.0 - performance.getAccuracy()) : 50.0;
        long daysSinceRevision = calculateDaysSinceRevision(lastAttemptByTopic.get(topic));
        int examImportance = EXAM_IMPORTANCE_MAP.getOrDefault(SubjectMapper.getSubject(topic), 5);
        double priorityScore = weaknessScore + (daysSinceRevision * 2.0) + examImportance;
        return new TopicScore(topic, priorityScore, weaknessScore, daysSinceRevision);
    }

    private long calculateDaysSinceRevision(LocalDateTime lastAttemptedAt) {
        if (lastAttemptedAt == null) {
            return 30;
        }
        long days = ChronoUnit.DAYS.between(lastAttemptedAt.toLocalDate(), LocalDate.now());
        if (days < 0) {
            days = 0;
        }
        return Math.min(days, 30);
    }

    private String computeWeaknessLevel(double weaknessScore) {
        if (weaknessScore >= 70.0) {
            return "HIGH";
        }
        if (weaknessScore >= 40.0) {
            return "MEDIUM";
        }
        return "LOW";
    }

    public List<QuestionDto> findDailyQuestions(List<String> topics, int count, String userEmail) {
        if (count <= 0) {
            count = 10;
        }
        System.out.println("Service received topics: " + topics + " count=" + count);
        List<Question> questions;

        if (topics == null || topics.isEmpty() || isMixedPractice(topics)) {
            if (userEmail != null && !userEmail.isBlank()) {
                questions = generateMixedPracticeQuestions(userEmail, count);
            } else {
                System.out.println("Using random practice query because no authenticated user was available");
                questions = questionRepository.findRandomQuestions(count);
            }
        } else {
            System.out.println("Using topic-filtered query");
            questions = questionRepository.findQuestionsByTopics(topics, count);
        }

        if (questions == null) {
            questions = List.of();
        }

        System.out.println("Question entities returned: " + questions.size());
        return questions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AnswerResultDto submitAnswer(String email, AnswerSubmissionDto submission) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Question question = questionRepository.findById(submission.getQuestionId()).orElseThrow();
        String subject = question.getSubject();
        if (subject == null || subject.isBlank()) {
            subject = SubjectMapper.getSubject(question.getTopic());
            question.setSubject(subject);
            questionRepository.save(question);
        }

        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(submission.getSelectedAnswer());
        LocalDateTime now = LocalDateTime.now();
        UserAnswer answer = new UserAnswer(user, question, subject, submission.getSelectedAnswer(), isCorrect, now, now);
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
        } else if (lastActive.equals(today)) {
            Integer streakCount = user.getStreakCount();
            if (streakCount == null || streakCount < 1) {
                user.setStreakCount(1);
            }
        } else if (lastActive.plusDays(1).equals(today)) {
            Integer streakCount = user.getStreakCount();
            int streak = streakCount == null ? 0 : streakCount;
            user.setStreakCount(Math.max(streak, 0) + 1);
        } else {
            user.setStreakCount(1);
        }
        user.setLastActiveDate(today);
        userRepository.save(user);
    }

    private List<Question> generateMixedPracticeQuestions(String email, int questionCount) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return questionRepository.findRandomQuestions(questionCount);
        }

        Set<Long> attemptedIds = userAnswerRepository.findAllByUser(user).stream()
                .map(UserAnswer::getQuestion)
                .filter(Objects::nonNull)
                .map(Question::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, Integer> subjectTargets = allocateSubjectCounts(questionCount);
        List<Question> mixedQuestions = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : subjectTargets.entrySet()) {
            if (entry.getValue() <= 0) {
                continue;
            }
            mixedQuestions.addAll(selectSubjectQuestions(entry.getKey(), attemptedIds, entry.getValue()));
        }

        if (mixedQuestions.size() < questionCount) {
            mixedQuestions.addAll(fillRemainingQuestions(mixedQuestions, attemptedIds, questionCount));
        }

        Collections.shuffle(mixedQuestions);
        return mixedQuestions.stream().distinct().limit(questionCount).collect(Collectors.toList());
    }

    private Map<String, Integer> allocateSubjectCounts(int totalCount) {
        Map<String, Integer> targetCounts = new LinkedHashMap<>();
        List<SubjectRemainder> remainders = new ArrayList<>();
        int assigned = 0;

        for (Map.Entry<String, Integer> entry : SUBJECT_WEIGHTS.entrySet()) {
            double exact = entry.getValue() * totalCount / 100.0;
            int base = (int) Math.floor(exact);
            targetCounts.put(entry.getKey(), base);
            remainders.add(new SubjectRemainder(entry.getKey(), exact - base));
            assigned += base;
        }

        int remaining = totalCount - assigned;
        remainders.sort(Comparator.comparingDouble(SubjectRemainder::getRemainder).reversed());
        for (int i = 0; i < remaining && i < remainders.size(); i++) {
            targetCounts.put(remainders.get(i).subject, targetCounts.get(remainders.get(i).subject) + 1);
        }

        return targetCounts;
    }

    private List<Question> selectSubjectQuestions(String subject, Set<Long> attemptedIds, int required) {
        List<Question> questions;
        if (attemptedIds == null || attemptedIds.isEmpty()) {
            questions = questionRepository.findQuestionsBySubject(subject, required);
        } else {
            questions = questionRepository.findUnattemptedQuestionsBySubject(subject, new ArrayList<>(attemptedIds), required);
        }

        if (questions.size() < required && attemptedIds != null && !attemptedIds.isEmpty()) {
            int remaining = required - questions.size();
            List<Question> fallback = questionRepository.findAttemptedQuestionsBySubject(subject, new ArrayList<>(attemptedIds), remaining);
            questions.addAll(fallback);
        }

        return questions;
    }

    private List<Question> fillRemainingQuestions(List<Question> selectedQuestions, Set<Long> attemptedIds, int targetCount) {
        Set<Long> selectedIds = selectedQuestions.stream()
                .map(Question::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        int remaining = targetCount - selectedQuestions.size();
        if (remaining <= 0) {
            return List.of();
        }

        List<Long> excludeIds = new ArrayList<>(selectedIds);
        if (attemptedIds != null && !attemptedIds.isEmpty()) {
            excludeIds.addAll(attemptedIds);
        }

        List<Question> fill = excludeIds.isEmpty()
                ? questionRepository.findRandomQuestions(remaining)
                : questionRepository.findQuestionsExcludingIds(excludeIds, remaining);

        if (fill.size() < remaining && !selectedIds.isEmpty()) {
            int stillNeeded = remaining - fill.size();
            fill.addAll(questionRepository.findQuestionsExcludingIds(new ArrayList<>(selectedIds), stillNeeded));
        }

        return fill;
    }

    private boolean isMixedPractice(List<String> topics) {
        return topics.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .anyMatch(topic -> "Mixed Practice".equalsIgnoreCase(topic));
    }

    private QuestionDto mapToDto(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setTopic(question.getTopic());
        dto.setSubject(question.getSubject());
        dto.setCategory(question.getTopic());
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

    private static class TopicPerformance {
        private final double accuracy;
        private final int totalCount;

        public TopicPerformance(double accuracy, int totalCount) {
            this.accuracy = accuracy;
            this.totalCount = totalCount;
        }

        public double getAccuracy() {
            return accuracy;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    private static class TopicScore {
        private final String topic;
        private final double priorityScore;
        private final double weaknessScore;
        private final long daysSinceRevision;

        public TopicScore(String topic, double priorityScore, double weaknessScore, long daysSinceRevision) {
            this.topic = topic;
            this.priorityScore = priorityScore;
            this.weaknessScore = weaknessScore;
            this.daysSinceRevision = daysSinceRevision;
        }

        public String getTopic() {
            return topic;
        }

        public double getPriorityScore() {
            return priorityScore;
        }

        public double getWeaknessScore() {
            return weaknessScore;
        }
    }

    private static class SubjectRemainder {
        private final String subject;
        private final double remainder;

        public SubjectRemainder(String subject, double remainder) {
            this.subject = subject;
            this.remainder = remainder;
        }

        public double getRemainder() {
            return remainder;
        }
    }
}
