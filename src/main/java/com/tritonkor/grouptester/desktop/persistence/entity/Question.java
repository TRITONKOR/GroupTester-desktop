package com.tritonkor.grouptester.desktop.persistence.entity;

import java.util.List;
import java.util.UUID;

/**
 * The {@code Question} class represents a question with its associated properties.
 */
public class Question extends Entity {

    private String text;
    private List<Answer> answers;
    private UUID testId;
    private Test test;

    public Question(UUID id, String text, List<Answer> answers, UUID testId, Test test) {
        super(id);
        this.text = text;
        this.answers = answers;
        this.testId = testId;
        this.test = test;
    }

    public Question() {
        super(null);
    }

    /**
     * Returns a {@code QuestionBuilderId} instance to start building a {@code Question}.
     *
     * @return A {@code QuestionBuilderId} instance.
     */
    public static QuestionBuilderId builder() {
        return id -> answers -> testId -> test -> content -> () -> new Question(id, answers, testId, test,
                content);
    }

    /**
     * Interface for the {@code Question} builder to set the ID.
     */
    public interface QuestionBuilderId {

        QuestionBuilderText id(UUID id);
    }

    public interface QuestionBuilderText {

        QuestionBuilderAnswers text(String text);
    }

    public interface QuestionBuilderAnswers {

        QuestionBuilderTestId answers(List<Answer> answers);
    }

    public interface QuestionBuilderTestId {

        QuestionBuilderTest testId(UUID testId);
    }

    public interface QuestionBuilderTest {

        QuestionBuilder test(Test test);
    }

    /**
     * Interface for the final steps of the {@code Question} builder.
     */
    public interface QuestionBuilder {

        Question build();
    }

    public String getText() {
        return text;
    }

    public UUID getTestId() {
        return testId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
