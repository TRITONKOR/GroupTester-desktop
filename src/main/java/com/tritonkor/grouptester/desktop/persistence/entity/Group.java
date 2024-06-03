package com.tritonkor.grouptester.desktop.persistence.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a group entity in the GroupTester desktop application.
 */
public class Group extends Entity{
    private String name;
    private String code;
    private Boolean readyToTesting;
    private Boolean canApplyNewUsers;
    private Test test;
    private Map<User, Boolean> users;
    private Map<User, Mark> results;

    /**
     * Constructs a group with specified attributes.
     *
     * @param id               The unique identifier of the group.
     * @param name             The name of the group.
     * @param code             The code of the group.
     * @param readyToTesting   Indicates whether the group is ready for testing.
     * @param canApplyNewUsers Indicates whether new users can apply to join the group.
     * @param test             The test associated with the group.
     */
    public Group(UUID id, String name, String code, Boolean readyToTesting, Boolean canApplyNewUsers, Test test) {
        super(id);
        this.name = name;
        this.code = code;
        this.readyToTesting = readyToTesting;
        this.canApplyNewUsers = canApplyNewUsers;
        this.test = test;
        this.users = new HashMap<>();
        this.results = new HashMap<>();
    }

    /**
     * Constructs an empty group.
     */
    public Group() {
        super(null);
    }

    public static GroupBuilderId builder() {
        return id -> name -> code -> readyToTesting -> canApplyNewUsers -> test -> () -> new Group(id, name, code, readyToTesting, canApplyNewUsers, test);
    }

    public interface GroupBuilderId {
        GroupBuilderName id(UUID id);
    }

    public interface GroupBuilderName {
        GroupBuilderCode name(String name);
    }

    public interface GroupBuilderCode {
        GroupBuilderReadyToTesting code(String code);
    }

    public interface GroupBuilderReadyToTesting {
        GroupBuilderCanApplyNewUsers readyToTesting(Boolean readyToTesting);
    }

    public interface GroupBuilderCanApplyNewUsers {
        GroupBuilderTest canApplyNewUsers(Boolean canApplyNewUsers);
    }

    public interface GroupBuilderTest {
        GroupBuilder test(Test test);
    }

    public interface GroupBuilder {
        Group build();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getCanApplyNewUsers() {
        return canApplyNewUsers;
    }

    public Boolean getReadyToTesting() {
        return readyToTesting;
    }

    public Map<User, Boolean> getUsers() {
        return users;
    }

    public Map<User, Mark> getResults() {
        return results;
    }
}
