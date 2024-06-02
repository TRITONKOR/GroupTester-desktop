package com.tritonkor.grouptester.desktop.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The {@code Result} class represents a result of a test for a specific user.
 */
public class Result extends Entity {

    private UUID ownerId;
    private User owner;
    private UUID testId;
    private Test test;
    private String groupCode;
    private Mark mark;
    private LocalDateTime createdAt;

    public Result(UUID id, UUID ownerId, User owner, UUID testId, Test test, String groupCode,
             Mark mark, LocalDateTime createdAt) {
        super(id);
        this.ownerId = ownerId;
        this.owner = owner;
        this.testId = testId;
        this.test = test;
        this.groupCode = groupCode;
        this.mark = mark;
        this.createdAt = createdAt;
    }

    public Result() {
        super(null);
    }

    /**
     * Returns a {@code ResultBuilderId} instance to start building a {@code Result}.
     *
     * @return A {@code ResultBuilderId} instance.
     */
    public static ResultBuilderId builder() {
        return id -> ownerId -> owner -> testId -> test -> groupCode -> mark -> createdAt -> () -> new Result(
                id, ownerId, owner, testId, test, groupCode, mark, createdAt);
    }

    /**
     * Interface for the {@code Result} builder to set the ID.
     */
    public interface ResultBuilderId {

        ResultBuilderOwnerId id(UUID id);
    }

    public interface ResultBuilderOwnerId {

        ResultBuilderOwner ownerId(UUID ownerId);
    }

    public interface ResultBuilderOwner {

        ResultBuilderTestId owner(User owner);
    }

    public interface ResultBuilderTestId {

        ResultBuilderTest testId(UUID testId);
    }

    public interface ResultBuilderTest {

        ResultBuilderGroupCode test(Test test);
    }

    public interface ResultBuilderGroupCode {

        ResultBuilderMark groupCode(String groupCode);
    }

    public interface ResultBuilderMark {

        ResultBuilderCreatedAt mark(Mark mark);
    }

    public interface ResultBuilderCreatedAt {

        ResultBuilder createdAt(LocalDateTime createdAt);
    }

    /**
     * Interface for the final steps of the {@code Result} builder.
     */
    public interface ResultBuilder {

        Result build();
    }

    public Mark getMark() {
        return mark;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public UUID getTestId() {
        return testId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    /**
     * Gets the creation timestamp of the result.
     *
     * @return The creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
