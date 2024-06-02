package com.tritonkor.grouptester.desktop.persistence.entity;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

/**
 * The {@code Question} class represents a question with its associated properties.
 */

public class Question extends Entity {

    private String text;
    private byte[] image;
    private List<Answer> answers;
    private UUID testId;
    private Test test;

    public Question(UUID id, String text, byte[] image, List<Answer> answers, UUID testId, Test test) {
        super(id);
        this.text = text;
        this.image = image;
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
        return id -> text -> image -> answers -> testId -> test  -> () -> new Question(id, text, image, answers,
                testId, test);
    }

    /**
     * Interface for the {@code Question} builder to set the ID.
     */
    public interface QuestionBuilderId {

        QuestionBuilderText id(UUID id);
    }

    public interface QuestionBuilderText {

        QuestionBuilderImage text(String text);
    }

    public interface QuestionBuilderImage {

        QuestionBuilderAnswers image(byte[] image);
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public UUID getTestId() {
        return testId;
    }

    public Test getTest() {
        return test;
    }
}
