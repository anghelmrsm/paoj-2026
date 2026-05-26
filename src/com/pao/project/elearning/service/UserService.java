package com.pao.project.elearning.service;

import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.model.Student;
import com.pao.project.elearning.repository.InstructorRepository;
import com.pao.project.elearning.repository.StudentRepository;

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
    private final StudentRepository studentRepository = new StudentRepository();
    private final InstructorRepository instructorRepository = new InstructorRepository();

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public void addStudent(Student student) {
        if (students.putIfAbsent(student.getEmail(), student) == null) {
            studentRepository.save(student);
        }
        AuditService.getInstance().logAction("add_student");
    }

    public void addInstructor(Instructor instructor) {
        if (instructors.putIfAbsent(instructor.getEmail(), instructor) == null) {
            instructorRepository.save(instructor);
        }
        AuditService.getInstance().logAction("add_instructor");
    }

    public Optional<Student> findStudentByEmail(String email) {
        AuditService.getInstance().logAction("find_student_by_email");
        return studentRepository.findByEmail(email);
    }

    public Optional<Instructor> findInstructorByEmail(String email) {
        AuditService.getInstance().logAction("find_instructor_by_email");
        return instructorRepository.findByEmail(email);
    }

    public List<Student> listAllStudents() {
        AuditService.getInstance().logAction("list_all_students");
        return Collections.unmodifiableList(studentRepository.findAll());
    }

    public List<Instructor> listAllInstructors() {
        AuditService.getInstance().logAction("list_all_instructors");
        return Collections.unmodifiableList(instructorRepository.findAll());
    }
}
