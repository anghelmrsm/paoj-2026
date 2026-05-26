package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Book;
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

public class BookRepository implements Repository<Book, Long> {
    private Connection connection() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException ex) { throw new SQLException(ex); }
    }
    @Override public void save(Book book) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "INSERT INTO book (title, author_id, available) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle()); ps.setLong(2, book.getAuthorId()); ps.setInt(3, book.isAvailable() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) book.setId(keys.getLong(1)); }
        }
    }
    @Override public Optional<Book> findById(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "SELECT id, title, author_id, available FROM book WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        }
    }
    @Override public List<Book> findAll() throws SQLException {
        List<Book> result = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(
                "SELECT id, title, author_id, available FROM book ORDER BY id");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) result.add(map(rs)); }
        return result;
    }
    @Override public void update(Book book) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "UPDATE book SET title=?, author_id=?, available=? WHERE id=?")) {
            ps.setString(1, book.getTitle()); ps.setLong(2, book.getAuthorId());
            ps.setInt(3, book.isAvailable() ? 1 : 0); ps.setLong(4, book.getId()); ps.executeUpdate();
        }
    }
    @Override public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("DELETE FROM book WHERE id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }
    private Book map(ResultSet rs) throws SQLException {
        Book book = new Book(rs.getString("title"), rs.getLong("author_id"));
        book.setId(rs.getLong("id")); book.setAvailable(rs.getInt("available") == 1); return book;
    }
}
