package com.tnpsc.app.util;

import java.util.List;
import java.util.Map;

public class SubjectMapper {

    private static final Map<String, String> TOPIC_TO_SUBJECT = Map.ofEntries(
            Map.entry("Fundamental Rights", "Polity"),
            Map.entry("Parliament", "Polity"),
            Map.entry("Indian Polity", "Polity"),
            Map.entry("Constitution", "Polity"),
            Map.entry("Directive Principles", "Polity"),
            Map.entry("Judiciary", "Polity"),
            Map.entry("President", "Polity"),
            Map.entry("Governor", "Polity"),
            Map.entry("Amendment", "Polity"),
            Map.entry("Preamble", "Polity"),
            Map.entry("Election Commission", "Polity"),
            Map.entry("Local Government", "Polity"),
            Map.entry("Ancient History", "History"),
            Map.entry("Medieval History", "History"),
            Map.entry("Modern History", "History"),
            Map.entry("Tamil History", "History"),
            Map.entry("Indian National Movement", "History"),
            Map.entry("Mughal Empire", "History"),
            Map.entry("Chola Dynasty", "History"),
            Map.entry("Maurya Empire", "History"),
            Map.entry("British Rule", "History"),
            Map.entry("World History", "History"),
            Map.entry("Physics", "Science"),
            Map.entry("Biology", "Science"),
            Map.entry("Chemistry", "Science"),
            Map.entry("Environment", "Science"),
            Map.entry("Science and Technology", "Science"),
            Map.entry("Human Body", "Science"),
            Map.entry("Plants", "Science"),
            Map.entry("Space", "Science"),
            Map.entry("Indian Geography", "Geography"),
            Map.entry("Tamil Nadu Geography", "Geography"),
            Map.entry("World Geography", "Geography"),
            Map.entry("Physical Geography", "Geography"),
            Map.entry("Climate", "Geography"),
            Map.entry("Rivers", "Geography"),
            Map.entry("Soils", "Geography"),
            Map.entry("Economics", "Economics"),
            Map.entry("Indian Economy", "Economics"),
            Map.entry("Budget", "Economics"),
            Map.entry("Banking", "Economics"),
            Map.entry("Agriculture", "Economics"),
            Map.entry("Five Year Plan", "Economics"),
            Map.entry("Poverty", "Economics"),
            Map.entry("Current Affairs", "Current Affairs"),
            Map.entry("Government Schemes", "Current Affairs"),
            Map.entry("Awards", "Current Affairs"),
            Map.entry("Sports", "Current Affairs"),
            Map.entry("National Events", "Current Affairs")
    );

    private static final Map<String, List<String>> SUBJECT_TO_TOPICS = Map.of(
            "Polity", List.of("Fundamental Rights", "Parliament", "Indian Polity", "Constitution", "Directive Principles", "Judiciary", "President", "Governor", "Amendment", "Preamble", "Election Commission", "Local Government"),
            "History", List.of("Ancient History", "Medieval History", "Modern History", "Tamil History", "Indian National Movement", "Mughal Empire", "Chola Dynasty", "Maurya Empire", "British Rule", "World History"),
            "Science", List.of("Physics", "Biology", "Chemistry", "Environment", "Science and Technology", "Human Body", "Plants", "Space"),
            "Geography", List.of("Indian Geography", "Tamil Nadu Geography", "World Geography", "Physical Geography", "Climate", "Rivers", "Soils"),
            "Economics", List.of("Economics", "Indian Economy", "Budget", "Banking", "Agriculture", "Five Year Plan", "Poverty"),
            "Current Affairs", List.of("Current Affairs", "Government Schemes", "Awards", "Sports", "National Events")
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
