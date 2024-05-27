package com.tritonkor.grouptester.desktop.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.NoArgsConstructor;

/**
 * The {@code Test} class represents a test in the system.
 * It extends the {@code Entity} class and implements {@code Comparable<Test>}.
 */
public class Test extends Entity {

    private String title;
    private List<Tag> tags;
    private UUID ownerId;
    private User owner;
    private List<Question> questions;
    private LocalDateTime createDate;

    public Test(UUID id, String title, UUID ownerId, User owner,
            List<Question> questions, List<Tag> tags, LocalDateTime createDate) {
        super(id);
        this.title = title;
        this.tags = tags;
        this.ownerId = ownerId;
        this.owner = owner;
        this.questions = questions;
        this.createDate = createDate;
    }

    public Test() {
        super(null);
    }

    /**
     * Returns a {@code TestBuilderId} instance to start building a {@code Test}.
     *
     * @return A {@code TestBuilderId} instance.
     */
    public static TestBuilderId builder() {
        return id -> title -> ownerId -> owner -> questions -> tags -> createDate -> () -> new Test(
                id, title,
                ownerId, owner, questions, tags, createDate);
    }

    /**
     * Interface for the {@code Test} builder to set the ID.
     */
    public interface TestBuilderId {

        TestBuilderTitle id(UUID id);
    }

    /**
     * Interface for the {@code Test} builder to set the title.
     */
    public interface TestBuilderTitle {

        TestBuilderOwnerId title(String title);
    }


    public interface TestBuilderOwnerId {

        TestBuilderOwner ownerId(UUID ownerId);
    }

    public interface TestBuilderOwner {

        TestBuilderQuestions owner(User owner);
    }

    public interface TestBuilderQuestions {

        TestBuilderTags questions(List<Question> questions);
    }

    public interface TestBuilderTags {

        TestBuilderCreateDate tags(List<Tag> tags);
    }

    /**
     * Interface for the {@code Test} builder to set the creation date.
     */
    public interface TestBuilderCreateDate {

        TestBuilder createDate(LocalDateTime createDate);
    }

    /**
     * Interface for the final steps of the {@code Test} builder.
     */
    public interface TestBuilder {

        Test build();
    }

    /**
     * Gets the title of the test.
     *
     * @return The test's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the unique identifier of the owner of the test.
     *
     * @return The owner's unique identifier.
     */
    public UUID getOwnerId() {
        return ownerId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Computes a hash code for this test based on its title.
     *
     * @return A hash code value for this test.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
