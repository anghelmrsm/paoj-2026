package com.pao.project.elearning.service;

import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, Instructor> instructors = new HashMap<>();

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void addStudent(Student student) {
        students.putIfAbsent(student.getEmail(), student);
        AuditService.getInstance().logAction("add_student");
    }

    public void addInstructor(Instructor instructor) {
        instructors.putIfAbsent(instructor.getEmail(), instructor);
        AuditService.getInstance().logAction("add_instructor");
    }

    public Optional<Student> findStudentByEmail(String email) {
        AuditService.getInstance().logAction("find_student_by_email");
        return Optional.ofNullable(students.get(email));
    }

    public Optional<Instructor> findInstructorByEmail(String email) {
        AuditService.getInstance().logAction("find_instructor_by_email");
        return Optional.ofNullable(instructors.get(email));
    }

    public List<Student> listAllStudents() {
        AuditService.getInstance().logAction("list_all_students");
        return Collections.unmodifiableList(new ArrayList<>(students.values()));
    }

    public List<Instructor> listAllInstructors() {
        AuditService.getInstance().logAction("list_all_instructors");
        return Collections.unmodifiableList(new ArrayList<>(instructors.values()));
    }
}
