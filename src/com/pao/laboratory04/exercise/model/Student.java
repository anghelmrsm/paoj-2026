package com.pao.laboratory04.exercise.model;

import com.pao.laboratory04.exercise.exception.InvalidGradeException;
import com.pao.laboratory04.exercise.exception.InvalidStudentException;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String name;
    private final int age;
    private final Map<Subject, Double> grades;

    public Student(String name, int age) {
        if (name == null || name.isBlank()) {
            throw new InvalidStudentException("Numele studentului nu poate fi gol");
        }
        if (age < 18 || age > 60) {
            throw new InvalidStudentException("Varsta " + age + " nu este valida (18-60)");
        }
        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public void addGrade(Subject subject, double grade) {
        if (subject == null) {
            throw new InvalidGradeException("Materia nu poate fi null");
        }
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota " + grade + " nu este valida (1-10)");
        }
        grades.put(subject, grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) {
            return 0;
        }
        double sum = 0;
        for (double grade : grades.values()) {
            sum += grade;
        }
        return sum / grades.size();
    }

    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, avg=%.2f}", name, age, getAverage());
    }
}
