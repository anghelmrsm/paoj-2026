package com.pao.project.elearning.repository;

import com.pao.project.elearning.model.Course;
import com.pao.project.elearning.model.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizRepository extends JdbcRepositorySupport implements Repository<Quiz, String> {
    private final CourseRepository courseRepository = new CourseRepository();

    @Override
    public void save(Quiz quiz) {
        String sql = "INSERT INTO quizzes (id, title, course_code, max_score) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, quiz.getId());
            statement.setString(2, quiz.getTitle());
            statement.setString(3, quiz.getCourse().getCode().toString());
            statement.setInt(4, quiz.getMaxScore());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("salvare quiz", ex);
        }
    }

    @Override
    public Optional<Quiz> findById(String id) {
        String sql = "SELECT id, title, course_code, max_score FROM quizzes WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? Optional.of(map(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw failure("cautare quiz", ex);
        }
    }

    @Override
    public List<Quiz> findAll() {
        List<Quiz> quizzes = new ArrayList<>();
        try (PreparedStatement statement = connection().prepareStatement(
                "SELECT id, title, course_code, max_score FROM quizzes ORDER BY id");
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                quizzes.add(map(result));
            }
            return quizzes;
        } catch (SQLException ex) {
            throw failure("listare quiz-uri", ex);
        }
    }

    @Override
    public void update(Quiz quiz) {
        String sql = "UPDATE quizzes SET title = ?, course_code = ?, max_score = ? WHERE id = ?";
        try (PreparedStatement statement = connection().prepareStatement(sql)) {
            statement.setString(1, quiz.getTitle());
            statement.setString(2, quiz.getCourse().getCode().toString());
            statement.setInt(3, quiz.getMaxScore());
            statement.setString(4, quiz.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("actualizare quiz", ex);
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement statement = connection().prepareStatement("DELETE FROM quizzes WHERE id = ?")) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw failure("stergere quiz", ex);
        }
    }

    private Quiz map(ResultSet result) throws SQLException {
        Course course = courseRepository.findByCodeValue(result.getString("course_code"))
                .orElseThrow(() -> new IllegalStateException("Curs lipsa pentru quiz."));
        return new Quiz(result.getString("id"), result.getString("title"), course,
                result.getInt("max_score"));
    }
}
