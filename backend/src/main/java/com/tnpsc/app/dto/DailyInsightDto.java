package com.tnpsc.app.dto;

public class DailyInsightDto {

    private String topic;

    private String title;

    private String explanation;

    private String tnpscTip;

    private String miniQuiz;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTnpscTip() {
        return tnpscTip;
    }

    public void setTnpscTip(String tnpscTip) {
        this.tnpscTip = tnpscTip;
    }

    public String getMiniQuiz() {
        return miniQuiz;
    }

    public void setMiniQuiz(String miniQuiz) {
        this.miniQuiz = miniQuiz;
    }
}
