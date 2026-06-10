package com.tnpsc.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String subject;

    @Column(name = "question_en", columnDefinition = "text")
    private String questionEn;

    @Column(name = "question_ta", columnDefinition = "text")
    private String questionTa;

    @Column(name = "option_a_en", columnDefinition = "text")
    private String optionAEn;

    @Column(name = "option_a_ta", columnDefinition = "text")
    private String optionATa;

    @Column(name = "option_b_en", columnDefinition = "text")
    private String optionBEn;

    @Column(name = "option_b_ta", columnDefinition = "text")
    private String optionBTa;

    @Column(name = "option_c_en", columnDefinition = "text")
    private String optionCEn;

    @Column(name = "option_c_ta", columnDefinition = "text")
    private String optionCTa;

    @Column(name = "option_d_en", columnDefinition = "text")
    private String optionDEn;

    @Column(name = "option_d_ta", columnDefinition = "text")
    private String optionDTa;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "explanation_en", columnDefinition = "text")
    private String explanationEn;

    @Column(name = "explanation_ta", columnDefinition = "text")
    private String explanationTa;

    public Question() {
    }

    public Question(String topic, String subject, String questionEn, String questionTa, String optionAEn, String optionATa, String optionBEn, String optionBTa, String optionCEn, String optionCTa, String optionDEn, String optionDTa, String correctAnswer, String explanationEn, String explanationTa) {
        this.topic = topic;
        this.subject = subject;
        this.questionEn = questionEn;
        this.questionTa = questionTa;
        this.optionAEn = optionAEn;
        this.optionATa = optionATa;
        this.optionBEn = optionBEn;
        this.optionBTa = optionBTa;
        this.optionCEn = optionCEn;
        this.optionCTa = optionCTa;
        this.optionDEn = optionDEn;
        this.optionDTa = optionDTa;
        this.correctAnswer = correctAnswer;
        this.explanationEn = explanationEn;
        this.explanationTa = explanationTa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuestionEn() {
        return questionEn;
    }

    public void setQuestionEn(String questionEn) {
        this.questionEn = questionEn;
    }

    public String getQuestionTa() {
        return questionTa;
    }

    public void setQuestionTa(String questionTa) {
        this.questionTa = questionTa;
    }

    public String getOptionAEn() {
        return optionAEn;
    }

    public void setOptionAEn(String optionAEn) {
        this.optionAEn = optionAEn;
    }

    public String getOptionATa() {
        return optionATa;
    }

    public void setOptionATa(String optionATa) {
        this.optionATa = optionATa;
    }

    public String getOptionBEn() {
        return optionBEn;
    }

    public void setOptionBEn(String optionBEn) {
        this.optionBEn = optionBEn;
    }

    public String getOptionBTa() {
        return optionBTa;
    }

    public void setOptionBTa(String optionBTa) {
        this.optionBTa = optionBTa;
    }

    public String getOptionCEn() {
        return optionCEn;
    }

    public void setOptionCEn(String optionCEn) {
        this.optionCEn = optionCEn;
    }

    public String getOptionCTa() {
        return optionCTa;
    }

    public void setOptionCTa(String optionCTa) {
        this.optionCTa = optionCTa;
    }

    public String getOptionDEn() {
        return optionDEn;
    }

    public void setOptionDEn(String optionDEn) {
        this.optionDEn = optionDEn;
    }

    public String getOptionDTa() {
        return optionDTa;
    }

    public void setOptionDTa(String optionDTa) {
        this.optionDTa = optionDTa;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
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
}
