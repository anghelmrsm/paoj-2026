package com.pao.project.elearning.service;

import com.pao.project.elearning.exception.PersistenceException;
import com.pao.project.elearning.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ReportService {
    private static final ReportService INSTANCE = new ReportService();

    private ReportService() {
    }

    public static ReportService getInstance() {
        return INSTANCE;
    }

    public List<String> listCoursesWithEnrollmentCount() {
        String sql = "SELECT c.title, i.name AS instructor_name, COUNT(e.student_id) AS student_count "
                + "FROM courses c JOIN instructors i ON i.id = c.instructor_id "
                + "LEFT JOIN enrollments e ON e.course_code = c.code "
                + "GROUP BY c.code, c.title, i.name ORDER BY student_count DESC, c.title";
        List<String> rows = query(sql, result -> String.format("%s | instructor=%s | studenti=%d",
                result.getString("title"), result.getString("instructor_name"),
                result.getInt("student_count")));
        AuditService.getInstance().logAction("report_courses_with_enrollment_count");
        return rows;
    }

    public List<String> listScoresWithStudentAndCourse() {
        String sql = "SELECT s.name AS student_name, q.title AS quiz_title, c.title AS course_title, sc.score "
                + "FROM scores sc JOIN students s ON s.id = sc.student_id "
                + "JOIN quizzes q ON q.id = sc.quiz_id JOIN courses c ON c.code = q.course_code "
                + "ORDER BY s.name, c.title";
        List<String> rows = query(sql, result -> String.format("%s | %s (%s) | scor=%d",
                result.getString("student_name"), result.getString("quiz_title"),
                result.getString("course_title"), result.getInt("score")));
        AuditService.getInstance().logAction("report_scores_with_course");
        return rows;
    }

    public List<String> listInstructorActivity() {
        String sql = "SELECT i.name AS instructor_name, COUNT(DISTINCT c.code) AS courses, "
                + "COUNT(e.student_id) AS enrollments FROM instructors i "
                + "LEFT JOIN courses c ON c.instructor_id = i.id "
                + "LEFT JOIN enrollments e ON e.course_code = c.code "
                + "GROUP BY i.id, i.name ORDER BY i.name";
        List<String> rows = query(sql, result -> String.format("%s | cursuri=%d | inscrieri=%d",
                result.getString("instructor_name"), result.getInt("courses"),
                result.getInt("enrollments")));
        AuditService.getInstance().logAction("report_instructor_activity");
        return rows;
    }

    private List<String> query(String sql, RowMapper mapper) {
        List<String> rows = new ArrayList<>();
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                rows.add(mapper.map(result));
            }
            return rows;
        } catch (SQLException ex) {
            throw new PersistenceException("Nu s-a putut genera raportul.", ex);
        }
    }

    @FunctionalInterface
    private interface RowMapper {
        String map(ResultSet result) throws SQLException;
    }
}
