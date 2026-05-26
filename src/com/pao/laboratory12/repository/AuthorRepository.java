package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Author;
import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepository implements Repository<Author, Long> {
    private Connection connection() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException ex) { throw new SQLException(ex); }
    }

    @Override public void save(Author author) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "INSERT INTO author (name, country) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author.getName()); ps.setString(2, author.getCountry()); ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) author.setId(keys.getLong(1)); }
        }
    }
    @Override public Optional<Author> findById(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("SELECT id, name, country FROM author WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        }
    }
    @Override public List<Author> findAll() throws SQLException {
        List<Author> result = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement("SELECT id, name, country FROM author ORDER BY id");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) result.add(map(rs)); }
        return result;
    }
    @Override public void update(Author author) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("UPDATE author SET name=?, country=? WHERE id=?")) {
            ps.setString(1, author.getName()); ps.setString(2, author.getCountry()); ps.setLong(3, author.getId());
            ps.executeUpdate();
        }
    }
    @Override public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("DELETE FROM author WHERE id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }
    private Author map(ResultSet rs) throws SQLException {
        Author author = new Author(rs.getString("name"), rs.getString("country"));
        author.setId(rs.getLong("id")); return author;
    }
}
