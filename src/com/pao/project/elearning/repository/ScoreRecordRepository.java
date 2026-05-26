package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.model.ScoreRecord;
import com.pao.project.elearning.model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreRecordRepository extends JdbcRepositorySupport implements Repository<ScoreRecord, String> {
    private final QuizRepository quizRepository = new QuizRepository();
    private final StudentRepository studentRepository = new StudentRepository();

    @Override
    public void save(ScoreRecord score) {
        String sql = "INSERT INTO scores (quiz_id, student_id, score) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            bind(statement, score);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("salvare scor", ex);
        }
    }

    @Override
    public Optional<ScoreRecord> findById(String id) {
        String[] parts = id.split(":", 2);
        if (parts.length != 2) {
            return Optional.empty();
        }
        String sql = "SELECT quiz_id, student_id, score FROM scores WHERE quiz_id = ? AND student_id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, parts[0]);
            statement.setString(2, parts[1]);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare scor", ex);
        }
    }

    @Override
    public List<ScoreRecord> findAll() {
        List<ScoreRecord> scores = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(
                "SELECT quiz_id, student_id, score FROM scores ORDER BY quiz_id, student_id");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                scores.add(map(result));
            }
            return scores;
        } catch (SQLException ex) {
            throw failure("listare scoruri", ex);
        }
    }

    public List<ScoreRecord> findByStudent(Student student) {
        List<ScoreRecord> scores = new ArrayList<>();
        String sql = "SELECT quiz_id, student_id, score FROM scores WHERE student_id = ? ORDER BY quiz_id";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, student.getId());
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    scores.add(map(result));
                }
            }
            return scores;
        } catch (SQLException ex) {
            throw failure("scorurile studentului", ex);
        }
    }

    @Override
    public void update(ScoreRecord score) {
        String sql = "UPDATE scores SET score = ? WHERE quiz_id = ? AND student_id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setInt(1, score.getScore());
            statement.setString(2, score.getQuiz().getId());
            statement.setString(3, score.getStudent().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("actualizare scor", ex);
        }
    }

    @Override
    public void delete(String id) {
        String[] parts = id.split(":", 2);
        if (parts.length != 2) {
            return;
        }
        try (PreparedStatement statement = connection().prepareStatement(
                "DELETE FROM scores WHERE quiz_id = ? AND student_id = ?")) {
            statement.setString(1, parts[0]);
            statement.setString(2, parts[1]);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("stergere scor", ex);
        }
    }

    private void bind(PreparedStatement statement, ScoreRecord score) throws SQLException {
        statement.setString(1, score.getQuiz().getId());
        statement.setString(2, score.getStudent().getId());
        statement.setInt(3, score.getScore());
    }

    private ScoreRecord map(ResultSet result) throws SQLException {
        Quiz quiz = quizRepository.findById(result.getString("quiz_id"))
                .orElseThrow(() -> new IllegalStateException("Quiz lipsa pentru scor."));
        Student student = studentRepository.findById(result.getString("student_id"))
                .orElseThrow(() -> new IllegalStateException("Student lipsa pentru scor."));
        return new ScoreRecord(quiz, student, result.getInt("score"));
    }
}
