package com.tnpsc.app.dto;

import java.util.List;

public class StatSummaryDto {
    private Long totalAttempts;
    private Long correctAttempts;
    private Double accuracy;
    private Integer streakCount;
    private List<WeakTopic> weakTopics;

    public static class WeakTopic {
        private String topic;
        private Long correct;
        private Long total;

        public WeakTopic() {
        }

        public WeakTopic(String topic, Long correct, Long total) {
            this.topic = topic;
            this.correct = correct;
            this.total = total;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Long getCorrect() {
            return correct;
        }

        public void setCorrect(Long correct) {
            this.correct = correct;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }
    }

    public Long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(Long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public Long getCorrectAttempts() {
        return correctAttempts;
    }

    public void setCorrectAttempts(Long correctAttempts) {
        this.correctAttempts = correctAttempts;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(Integer streakCount) {
        this.streakCount = streakCount;
    }

    public List<WeakTopic> getWeakTopics() {
        return weakTopics;
    }

    public void setWeakTopics(List<WeakTopic> weakTopics) {
        this.weakTopics = weakTopics;
    }
}
