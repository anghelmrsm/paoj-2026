package com.pao.project.elearning.service;

import com.pao.project.elearning.exception.CourseNotFoundException;
import com.pao.project.elearning.exception.UserAlreadyEnrolledException;
import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.Enrollment;
import com.pao.project.elearning.model.Student;
import com.pao.project.elearning.exception.PersistenceException;
import com.pao.project.elearning.repository.CourseRepository;
import com.pao.project.elearning.repository.EnrollmentRepository;
import com.pao.project.elearning.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnrollmentService {
    private static final EnrollmentService INSTANCE = new EnrollmentService();
    private final Map<Course, Set<Student>> studentsByCourse = new HashMap<>();
    private final Map<Student, Set<Course>> coursesByStudent = new HashMap<>();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
    private final CourseRepository courseRepository = new CourseRepository();

    private EnrollmentService() {
    }

    public static EnrollmentService getInstance() {
        return INSTANCE;
    }

    public void enrollStudent(Student student, Course course) {
        if (course == null) {
            throw new CourseNotFoundException("Cursul nu exista");
        }

        studentsByCourse.computeIfAbsent(course, key -> new HashSet<>());
        coursesByStudent.computeIfAbsent(student, key -> new HashSet<>());

        if (studentsByCourse.get(course).contains(student)) {
            throw new UserAlreadyEnrolledException(
                    String.format("Studentul %s este deja inscris la cursul %s", student.getName(), course.getTitle()));
        }

        Connection connection = DatabaseConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            enrollmentRepository.save(connection, new Enrollment(student, course));
            courseRepository.incrementEnrollmentCount(connection, course.getCode());
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException rollbackError) {
                ex.addSuppressed(rollbackError);
            }
            throw new PersistenceException("Inscrierea nu a putut fi salvata.", ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new PersistenceException("Nu s-a putut restaura auto-commit.", ex);
            }
        }
        studentsByCourse.get(course).add(student);
        coursesByStudent.get(student).add(course);
        course.enroll(student);
        AuditService.getInstance().logAction("enroll_student");
    }

    public List<Student> listStudentsInCourse(Course course) {
        AuditService.getInstance().logAction("list_students_in_course");
        return enrollmentRepository.findStudentsForCourse(course);
    }

    public List<Course> listCoursesForStudent(Student student) {
        AuditService.getInstance().logAction("list_courses_for_student");
        return enrollmentRepository.findCoursesForStudent(student);
    }
}
