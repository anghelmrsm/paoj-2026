package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Reader;
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

public class ReaderRepository implements Repository<Reader, Long> {
    private Connection connection() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException ex) { throw new SQLException(ex); }
    }
    @Override public void save(Reader reader) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "INSERT INTO reader (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reader.getName()); ps.setString(2, reader.getEmail()); ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) reader.setId(keys.getLong(1)); }
        }
    }
    @Override public Optional<Reader> findById(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("SELECT id, name, email FROM reader WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        }
    }
    @Override public List<Reader> findAll() throws SQLException {
        List<Reader> result = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement("SELECT id, name, email FROM reader ORDER BY id");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) result.add(map(rs)); }
        return result;
    }
    @Override public void update(Reader reader) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("UPDATE reader SET name=?, email=? WHERE id=?")) {
            ps.setString(1, reader.getName()); ps.setString(2, reader.getEmail()); ps.setLong(3, reader.getId());
            ps.executeUpdate();
        }
    }
    @Override public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("DELETE FROM reader WHERE id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }
    private Reader map(ResultSet rs) throws SQLException {
        Reader reader = new Reader(rs.getString("name"), rs.getString("email"));
        reader.setId(rs.getLong("id")); return reader;
    }
}
