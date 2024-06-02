package com.tritonkor.grouptester.desktop.persistence.entity;

import java.util.UUID;

/**
 * The {@code Answer} class represents an answer entity with its associated properties.
 */
public class Answer extends Entity {

    private UUID questionId;
    private Question question;
    private String text;
    private Boolean isCorrect;

    public Answer(UUID id, UUID questionId, Question question, String text,
            Boolean isCorrect) {
        super(id);
        this.questionId = questionId;
        this.question = question;
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public Answer() {
        super(null);
    }

    /**
     * Provides a builder for creating instances of {@code Answer}.
     *
     * @return An {@code AnswerBuilderId} instance to start building an {@code Answer}.
     */
    public static AnswerBuilderId builder() {
        return id -> questionId -> question -> text -> isCorrect -> () -> new Answer(id,
                questionId, question, text, isCorrect);
    }

    /**
     * Interface for the {@code Answer} builder to set the ID.
     */
    public interface AnswerBuilderId {

        AnswerBuilderQuestionId id(UUID id);
    }

    public interface AnswerBuilderQuestionId {

        AnswerBuilderQuestion questionId(UUID questionId);
    }

    public interface AnswerBuilderQuestion {

        AnswerBuilderText question(Question question);
    }

    /**
     * Interface for the {@code Answer} builder to set the text.
     */
    public interface AnswerBuilderText {

        AnswerBuilderIsCorrect text(String text);
    }

    public interface AnswerBuilderIsCorrect {

        AnswerBuilder isCorrect(Boolean isCorrect);
    }

    /**
     * Interface for the final steps of the {@code Answer} builder.
     */
    public interface AnswerBuilder {

        Answer build();
    }

    public String getText() {
        return text;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "questionId=" + questionId +
                ", question=" + question +
                ", text='" + text + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
