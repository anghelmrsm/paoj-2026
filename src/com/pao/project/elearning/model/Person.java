package com.pao.project.elearning.model;

public abstract class Person {
    private final String id;
    private final String name;
    private final String email;

    protected Person(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("%s[id=%s, name=%s, email=%s]", getRole(), id, name, email);
    }
}
