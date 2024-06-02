package com.tritonkor.grouptester.desktop.persistence.entity;

import java.util.List;
import java.util.UUID;

public class Tag extends Entity{

    private String name;
    private List<Test> tests;

    public Tag(UUID id, String name, List<Test> tests) {
        super(id);
        this.name = name;
        this.tests = tests;
    }

    public Tag() {
        super(null);
    }

    public static TagBuilderId builder() {
        return id -> name -> tests -> () -> new Tag(id, name, tests);
    }

    public interface TagBuilderId {
        TagBuilderName id(UUID id);
    }

    public interface TagBuilderName {
        TagBuilderTests name(String username);
    }

    public interface TagBuilderTests {
        TagBuilder tests(List<Test> tests);
    }

    public interface TagBuilder {
        Tag build();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
