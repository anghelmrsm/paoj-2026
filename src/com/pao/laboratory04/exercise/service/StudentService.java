package com.pao.laboratory04.exercise.service;

import com.pao.laboratory04.exercise.exception.StudentNotFoundException;
import com.pao.laboratory04.exercise.model.Student;
import com.pao.laboratory04.exercise.model.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static final StudentService INSTANCE = new StudentService();

    private final List<Student> students = new ArrayList<>();

    private StudentService() {
    }

    public static StudentService getInstance() {
        return INSTANCE;
    }

    public void addStudent(String name, int age) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' exista deja");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost gasit");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti.");
            return;
        }
        for (Student student : students) {
            System.out.println(student + " note=" + student.getGrades());
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti.");
            return;
        }
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingDouble(Student::getAverage).reversed());
        for (int i = 0; i < sorted.size(); i++) {
            System.out.println((i + 1) + ". " + sorted.get(i));
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();

        for (Student student : students) {
            for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                sums.put(subject, sums.getOrDefault(subject, 0.0) + entry.getValue());
                counts.put(subject, counts.getOrDefault(subject, 0) + 1);
            }
        }

        Map<Subject, Double> averages = new HashMap<>();
        for (Map.Entry<Subject, Double> entry : sums.entrySet()) {
            Subject subject = entry.getKey();
            averages.put(subject, entry.getValue() / counts.get(subject));
        }
        return averages;
    }
}
