package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.CourseCode;
import com.pao.project.elearning.model.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository extends JdbcRepositorySupport implements Repository<Course, CourseCode> {
    private final InstructorRepository instructorRepository = new InstructorRepository();

    @Override
    public void save(Course course) {
        String sql = "INSERT INTO courses "
                + "(code, code_prefix, code_number, title, category, instructor_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, course.getCode().toString());
            statement.setString(2, course.getCode().getPrefix());
            statement.setInt(3, course.getCode().getNumber());
            statement.setString(4, course.getTitle());
            statement.setString(5, course.getCategory());
            statement.setString(6, course.getInstructor().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("salvare curs", ex);
        }
    }

    @Override
    public Optional<Course> findById(CourseCode code) {
        return findByCodeValue(code.toString());
    }

    public Optional<Course> findByCodeValue(String code) {
        String sql = "SELECT code_prefix, code_number, title, category, instructor_id "
                + "FROM courses WHERE code = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, code);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare curs", ex);
        }
    }

    @Override
    public List<Course> findAll() {
        return findByQuery("SELECT code_prefix, code_number, title, category, instructor_id "
                + "FROM courses ORDER BY category, title", null);
    }

    public List<Course> findByCategory(String category) {
        return findByQuery("SELECT code_prefix, code_number, title, category, instructor_id "
                + "FROM courses WHERE category = ? ORDER BY title", category);
    }

    @Override
    public void update(Course course) {
        String sql = "UPDATE courses SET title = ?, category = ?, instructor_id = ? WHERE code = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getCategory());
            statement.setString(3, course.getInstructor().getId());
            statement.setString(4, course.getCode().toString());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("actualizare curs", ex);
        }
    }

    @Override
    public void delete(CourseCode code) {
        try (PreparedStatement statement = connection().prepareStatement("DELETE FROM courses WHERE code = ?")) {
            statement.setString(1, code.toString());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("stergere curs", ex);
        }
    }

    public void incrementEnrollmentCount(Connection transaction, CourseCode code) throws SQLException {
        try (PreparedStatement statement = transaction.prepareStatement(
                "UPDATE courses SET enrolled_count = enrolled_count + 1 WHERE code = ?")) {
            statement.setString(1, code.toString());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Cursul nu exista in baza de date: " + code);
            }
        }
    }

    private List<Course> findByQuery(String sql, String parameter) {
        List<Course> courses = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            if (parameter != null) {
                statement.setString(1, parameter);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    courses.add(map(result));
                }
            }
            return courses;
        } catch (SQLException ex) {
            throw failure("listare cursuri", ex);
        }
    }

    private Course map(ResultSet result) throws SQLException {
        Instructor instructor = instructorRepository.findById(result.getString("instructor_id"))
                .orElseThrow(() -> new IllegalStateException("Instructor lipsa pentru curs."));
        return new Course(new CourseCode(result.getString("code_prefix"), result.getInt("code_number")),
                result.getString("title"), result.getString("category"), instructor);
    }
}
