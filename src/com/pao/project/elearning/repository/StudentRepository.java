package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository extends JdbcRepositorySupport implements Repository<Student, String> {
    @Override
    public void save(Student student) {
        String sql = "INSERT INTO students (id, name, email, major) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bind(statement, student);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("salvare student", ex);
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        String sql = "SELECT id, name, email, major FROM students WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare student", ex);
        }
    }

    public Optional<Student> findByEmail(String email) {
        String sql = "SELECT id, name, email, major FROM students WHERE email = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare student dupa email", ex);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, name, email, major FROM students ORDER BY name";
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                students.add(map(result));
            }
            return students;
        } catch (SQLException ex) {
            throw failure("listare studenti", ex);
        }
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, major = ? WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getMajor());
            statement.setString(4, student.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("actualizare student", ex);
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement statement = connection().prepareStatement("DELETE FROM students WHERE id = ?")) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("stergere student", ex);
        }
    }

    private void bind(PreparedStatement statement, Student student) throws SQLException {
        statement.setString(1, student.getId());
        statement.setString(2, student.getName());
        statement.setString(3, student.getEmail());
        statement.setString(4, student.getMajor());
    }

    private Student map(ResultSet result) throws SQLException {
        return new Student(result.getString("id"), result.getString("name"),
                result.getString("email"), result.getString("major"));
    }
}
