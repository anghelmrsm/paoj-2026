package com.pao.laboratory12.service;

import com.pao.laboratory12.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class LibraryService {
    private static final LibraryService INSTANCE = new LibraryService();
    private LibraryService() {}
    public static LibraryService getInstance() { return INSTANCE; }

    private Connection connection() throws SQLException {
        try { return DatabaseConnection.getInstance().getConnection(); }
        catch (IOException ex) { throw new SQLException(ex); }
    }

    public long borrowBook(long readerId, long bookId) throws SQLException {
        Connection connection = connection();
        connection.setAutoCommit(false);
        try {
            try (PreparedStatement check = connection.prepareStatement("SELECT available FROM book WHERE id=?")) {
                check.setLong(1, bookId);
                try (ResultSet result = check.executeQuery()) {
                    if (!result.next() || result.getInt("available") == 0) {
                        throw new SQLException("Cartea nu este disponibila.");
                    }
                }
            }
            long loanId;
            try (PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO loan (book_id, reader_id, loan_date) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                insert.setLong(1, bookId); insert.setLong(2, readerId);
                insert.setString(3, LocalDate.now().toString()); insert.executeUpdate();
                try (ResultSet keys = insert.getGeneratedKeys()) { keys.next(); loanId = keys.getLong(1); }
            }
            try (PreparedStatement update = connection.prepareStatement("UPDATE book SET available=0 WHERE id=?")) {
                update.setLong(1, bookId); update.executeUpdate();
            }
            connection.commit();
            return loanId;
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void returnBook(long loanId) throws SQLException {
        Connection connection = connection();
        connection.setAutoCommit(false);
        try {
            long bookId;
            try (PreparedStatement find = connection.prepareStatement("SELECT book_id FROM loan WHERE id=?")) {
                find.setLong(1, loanId);
                try (ResultSet result = find.executeQuery()) {
                    if (!result.next()) { throw new SQLException("Imprumut inexistent."); }
                    bookId = result.getLong(1);
                }
            }
            try (PreparedStatement loan = connection.prepareStatement("UPDATE loan SET return_date=? WHERE id=?");
                 PreparedStatement book = connection.prepareStatement("UPDATE book SET available=1 WHERE id=?")) {
                loan.setString(1, LocalDate.now().toString()); loan.setLong(2, loanId); loan.executeUpdate();
                book.setLong(1, bookId); book.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<String> getActiveLoansWithDetails() throws SQLException {
        return query("SELECT l.id, b.title, r.name, l.loan_date FROM loan l "
                + "JOIN book b ON b.id=l.book_id JOIN reader r ON r.id=l.reader_id "
                + "WHERE l.return_date IS NULL ORDER BY l.id",
                result -> "Loan#" + result.getLong(1) + " | " + result.getString(2) + " -> "
                        + result.getString(3) + " | " + result.getString(4));
    }

    public List<String> getTopBorrowedBooksWithAuthor() throws SQLException {
        return query("SELECT b.title, a.name, COUNT(l.id) FROM book b JOIN author a ON a.id=b.author_id "
                + "LEFT JOIN loan l ON l.book_id=b.id GROUP BY b.id, b.title, a.name ORDER BY COUNT(l.id) DESC",
                result -> result.getString(1) + " de " + result.getString(2) + ": " + result.getLong(3));
    }

    public List<String> getLoansCountPerReader() throws SQLException {
        return query("SELECT r.name, COUNT(l.id) FROM reader r LEFT JOIN loan l ON l.reader_id=r.id "
                + "GROUP BY r.id, r.name ORDER BY COUNT(l.id) DESC",
                result -> result.getString(1) + ": " + result.getLong(2));
    }

    private List<String> query(String sql, RowFormatter formatter) throws SQLException {
        List<String> rows = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) { rows.add(formatter.format(result)); }
        }
        return rows;
    }

    @FunctionalInterface
    private interface RowFormatter { String format(ResultSet result) throws SQLException; }
}
