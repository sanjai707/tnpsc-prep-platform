package com.tnpsc.app.dto;

public class AnswerResultDto {
    private boolean correct;
    private String explanationEn;
    private String explanationTa;
    private String correctAnswer;

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getExplanationEn() {
        return explanationEn;
    }

    public void setExplanationEn(String explanationEn) {
        this.explanationEn = explanationEn;
    }

    public String getExplanationTa() {
        return explanationTa;
    }

    public void setExplanationTa(String explanationTa) {
        this.explanationTa = explanationTa;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
