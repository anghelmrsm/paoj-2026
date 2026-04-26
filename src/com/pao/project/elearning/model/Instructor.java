package com.pao.project.elearning.model;

public class Instructor extends Person {
    private final String expertise;

    public Instructor(String id, String name, String email, String expertise) {
        super(id, name, email);
        this.expertise = expertise;
    }

    public String getExpertise() {
        return expertise;
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    @Override
    public String toString() {
        return String.format("Instructor[id=%s, name=%s, email=%s, expertise=%s]",
                getId(), getName(), getEmail(), expertise);
    }
}
