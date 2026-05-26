package com.pao.laboratory12.repository;

import com.pao.laboratory12.model.Loan;
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

public class LoanRepository implements Repository<Loan, Long> {
    private Connection connection() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException ex) { throw new SQLException(ex); }
    }
    @Override public void save(Loan loan) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "INSERT INTO loan (book_id, reader_id, loan_date, return_date) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, loan.getBookId()); ps.setLong(2, loan.getReaderId());
            ps.setString(3, loan.getLoanDate()); ps.setString(4, loan.getReturnDate()); ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) loan.setId(keys.getLong(1)); }
        }
    }
    @Override public Optional<Loan> findById(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "SELECT id, book_id, reader_id, loan_date, return_date FROM loan WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        }
    }
    @Override public List<Loan> findAll() throws SQLException {
        List<Loan> result = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(
                "SELECT id, book_id, reader_id, loan_date, return_date FROM loan ORDER BY id");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) result.add(map(rs)); }
        return result;
    }
    @Override public void update(Loan loan) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(
                "UPDATE loan SET book_id=?, reader_id=?, loan_date=?, return_date=? WHERE id=?")) {
            ps.setLong(1, loan.getBookId()); ps.setLong(2, loan.getReaderId()); ps.setString(3, loan.getLoanDate());
            ps.setString(4, loan.getReturnDate()); ps.setLong(5, loan.getId()); ps.executeUpdate();
        }
    }
    @Override public void delete(Long id) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement("DELETE FROM loan WHERE id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }
    private Loan map(ResultSet rs) throws SQLException {
        Loan loan = new Loan(rs.getLong("book_id"), rs.getLong("reader_id"), rs.getString("loan_date"));
        loan.setId(rs.getLong("id")); loan.setReturnDate(rs.getString("return_date")); return loan;
    }
}
