package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.Enrollment;
import com.pao.project.elearning.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentRepository extends JdbcRepositorySupport implements Repository<Enrollment, String> {
    private final StudentRepository studentRepository = new StudentRepository();
    private final CourseRepository courseRepository = new CourseRepository();

    @Override
    public void save(Enrollment enrollment) {
        try {
            save(connection(), enrollment);
        } catch (SQLException ex) {
            throw failure("salvare inscriere", ex);
        }
    }

    public void save(Connection transaction, Enrollment enrollment) throws SQLException {
        String sql = "INSERT INTO enrollments (student_id, course_code, enrolled_at) VALUES (?, ?, ?)";
        try (PreparedStatement statement = transaction.prepareStatement(sql)) {
            statement.setString(1, enrollment.getStudent().getId());
            statement.setString(2, enrollment.getCourse().getCode().toString());
            statement.setString(3, enrollment.getEnrolledAt().toString());
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Enrollment> findById(String id) {
        String[] parts = id.split(":", 2);
        if (parts.length != 2) {
            return Optional.empty();
        }
        String sql = "SELECT student_id, course_code, enrolled_at FROM enrollments "
                + "WHERE student_id = ? AND course_code = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, parts[0]);
            statement.setString(2, parts[1]);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare inscriere", ex);
        }
    }

    @Override
    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT student_id, course_code, enrolled_at FROM enrollments ORDER BY enrolled_at";
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                enrollments.add(map(result));
            }
            return enrollments;
        } catch (SQLException ex) {
            throw failure("listare inscrieri", ex);
        }
    }

    @Override
    public void update(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET enrolled_at = ? WHERE student_id = ? AND course_code = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, enrollment.getEnrolledAt().toString());
            statement.setString(2, enrollment.getStudent().getId());
            statement.setString(3, enrollment.getCourse().getCode().toString());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("actualizare inscriere", ex);
        }
    }

    @Override
    public void delete(String id) {
        String[] parts = id.split(":", 2);
        if (parts.length != 2) {
            return;
        }
        try (PreparedStatement statement = connection().prepareStatement(
                "DELETE FROM enrollments WHERE student_id = ? AND course_code = ?")) {
            statement.setString(1, parts[0]);
            statement.setString(2, parts[1]);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("stergere inscriere", ex);
        }
    }

    public List<Student> findStudentsForCourse(Course course) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.id, s.name, s.email, s.major FROM students s "
                + "JOIN enrollments e ON e.student_id = s.id WHERE e.course_code = ? ORDER BY s.name";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, course.getCode().toString());
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    students.add(new Student(result.getString("id"), result.getString("name"),
                            result.getString("email"), result.getString("major")));
                }
            }
            return students;
        } catch (SQLException ex) {
            throw failure("studenti inscrisi la curs", ex);
        }
    }

    public List<Course> findCoursesForStudent(Student student) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.code FROM courses c "
                + "JOIN enrollments e ON e.course_code = c.code WHERE e.student_id = ? ORDER BY c.title";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, student.getId());
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    courseRepository.findByCodeValue(result.getString("code")).ifPresent(courses::add);
                }
            }
            return courses;
        } catch (SQLException ex) {
            throw failure("cursurile studentului", ex);
        }
    }

    private Enrollment map(ResultSet result) throws SQLException {
        Student student = studentRepository.findById(result.getString("student_id"))
                .orElseThrow(() -> new IllegalStateException("Student lipsa pentru inscriere."));
        Course course = courseRepository.findByCodeValue(result.getString("course_code"))
                .orElseThrow(() -> new IllegalStateException("Curs lipsa pentru inscriere."));
        return new Enrollment(student, course, LocalDateTime.parse(result.getString("enrolled_at")));
    }
}
