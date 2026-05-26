package com.pao.project.elearning.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Course implements Comparable<Course> {
    private final CourseCode code;
    private final String title;
    private final String category;
    private final Instructor instructor;
    private final Set<Student> enrolledStudents = new HashSet<>();

    public Course(CourseCode code, String title, String category, Instructor instructor) {
        this.code = code;
        this.title = title;
        this.category = category;
        this.instructor = instructor;
    }

    public CourseCode getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Set<Student> getEnrolledStudents() {
        return Collections.unmodifiableSet(enrolledStudents);
    }

    public boolean enroll(Student student) {
        return enrolledStudents.add(student);
    }

    @Override
    public String toString() {
        return String.format("Course[code=%s, title=%s, category=%s, instructor=%s]",
                code, title, category, instructor.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public int compareTo(Course other) {
        int byCategory = category.compareTo(other.category);
        if (byCategory != 0) {
            return byCategory;
        }
        return title.compareTo(other.title);
    }
}
