package com.tnpsc.app.util;

import java.util.List;
import java.util.Map;

public class SubjectMapper {

    private static final Map<String, String> TOPIC_TO_SUBJECT = Map.ofEntries(
            Map.entry("Fundamental Rights", "Polity"),
            Map.entry("Parliament", "Polity"),
            Map.entry("Indian Polity", "Polity"),
            Map.entry("Ancient History", "History"),
            Map.entry("Medieval History", "History"),
            Map.entry("Modern History", "History"),
            Map.entry("Physics", "Science"),
            Map.entry("Biology", "Science"),
            Map.entry("Economics", "Economics"),
            Map.entry("Current Affairs", "Current Affairs")
    );

    private static final Map<String, List<String>> SUBJECT_TO_TOPICS = Map.of(
            "Polity", List.of("Fundamental Rights", "Parliament", "Indian Polity"),
            "History", List.of("Ancient History", "Medieval History", "Modern History"),
            "Science", List.of("Physics", "Biology"),
            "Economics", List.of("Economics"),
            "Current Affairs", List.of("Current Affairs")
    );

    public static String getSubject(String topic) {
        if (topic == null || topic.isBlank()) {
            return "General";
        }
        return TOPIC_TO_SUBJECT.getOrDefault(topic.trim(), "General");
    }

    public static List<String> getTopicsForSubject(String subject) {
        if (subject == null || subject.isBlank()) {
            return List.of();
        }
        return SUBJECT_TO_TOPICS.getOrDefault(subject.trim(), List.of());
    }

    public static List<String> getAllTopics() {
        return List.copyOf(TOPIC_TO_SUBJECT.keySet());
    }
}
