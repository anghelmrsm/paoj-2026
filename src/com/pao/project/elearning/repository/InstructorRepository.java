package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Instructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructorRepository extends JdbcRepositorySupport implements Repository<Instructor, String> {
    @Override
    public void save(Instructor instructor) {
        String sql = "INSERT INTO instructors (id, name, email, expertise) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bind(statement, instructor);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("salvare instructor", ex);
        }
    }

    @Override
    public Optional<Instructor> findById(String id) {
        String sql = "SELECT id, name, email, expertise FROM instructors WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare instructor", ex);
        }
    }

    public Optional<Instructor> findByEmail(String email) {
        String sql = "SELECT id, name, email, expertise FROM instructors WHERE email = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare instructor dupa email", ex);
        }
    }

    @Override
    public List<Instructor> findAll() {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT id, name, email, expertise FROM instructors ORDER BY name";
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                instructors.add(map(result));
            }
            return instructors;
        } catch (SQLException ex) {
            throw failure("listare instructori", ex);
        }
    }

    @Override
    public void update(Instructor instructor) {
        String sql = "UPDATE instructors SET name = ?, email = ?, expertise = ? WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, instructor.getName());
            statement.setString(2, instructor.getEmail());
            statement.setString(3, instructor.getExpertise());
            statement.setString(4, instructor.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("actualizare instructor", ex);
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement statement = connection().prepareStatement(
                "DELETE FROM instructors WHERE id = ?")) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("stergere instructor", ex);
        }
    }

    private void bind(PreparedStatement statement, Instructor instructor) throws SQLException {
        statement.setString(1, instructor.getId());
        statement.setString(2, instructor.getName());
        statement.setString(3, instructor.getEmail());
        statement.setString(4, instructor.getExpertise());
    }

    private Instructor map(ResultSet result) throws SQLException {
        return new Instructor(result.getString("id"), result.getString("name"),
                result.getString("email"), result.getString("expertise"));
    }
}
