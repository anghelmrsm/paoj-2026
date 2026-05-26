package com.pao.project.elearning.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Enrollment {
    private final Student student;
    private final Course course;
    private final LocalDateTime enrolledAt;

    public Enrollment(Student student, Course course) {
        this(student, course, LocalDateTime.now());
    }

    public Enrollment(Student student, Course course, LocalDateTime enrolledAt) {
        this.student = student;
        this.course = course;
        this.enrolledAt = enrolledAt;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    @Override
    public String toString() {
        return String.format("Enrollment[student=%s, course=%s, date=%s]",
                student.getName(), course.getTitle(), enrolledAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
