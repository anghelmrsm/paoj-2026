package com.pao.project.elearning.model;

import java.util.Objects;

public class Student extends Person {
    private final String major;

    public Student(String id, String name, String email, String major) {
        super(id, name, email);
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return String.format("Student[id=%s, name=%s, email=%s, major=%s]",
                getId(), getName(), getEmail(), major);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(getEmail(), student.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
