package com.tritonkor.grouptester.desktop.persistence.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Group extends Entity{
    private String name;
    private String code;
    private Map<User, Boolean> users;

    public Group(UUID id, String name, String code) {
        super(id);
        this.name = name;
        this.code = code;
        this.users = new HashMap<>();
    }

    public Group() {
        super(null);
    }

    public static GroupBuilderId builder() {
        return id -> name -> code -> () -> new Group(id, name, code);
    }

    public interface GroupBuilderId {
        GroupBuilderName id(UUID id);
    }

    public interface GroupBuilderName {
        GroupBuilderCode name(String name);
    }

    public interface GroupBuilderCode {
        GroupBuilder code(String code);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<User, Boolean> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user, false);
    }

    public void removeUser(User user) {
        if(users.containsKey(user)) {
            users.remove(user);
        }
        else {
            System.out.println("The user is not in the group");
        }
    }
}
