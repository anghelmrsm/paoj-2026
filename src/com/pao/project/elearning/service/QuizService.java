package com.pao.project.elearning.service;

import com.pao.project.elearning.exception.QuizNotFoundException;
import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.model.ScoreRecord;
import com.pao.project.elearning.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuizService {
    private static final QuizService INSTANCE = new QuizService();
    private final Map<String, Quiz> quizzes = new HashMap<>();
    private final List<ScoreRecord> scoreRecords = new ArrayList<>();

    private QuizService() {
    }

    public static QuizService getInstance() {
        return INSTANCE;
    }

    public void addQuiz(Quiz quiz) {
        quizzes.putIfAbsent(quiz.getId(), quiz);
        AuditService.getInstance().logAction("add_quiz");
    }

    public Quiz findQuizById(String id) {
        Quiz quiz = quizzes.get(id);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz-ul nu a fost gasit: " + id);
        }
        return quiz;
    }

    public void gradeQuiz(Quiz quiz, Student student, int score) {
        if (!quizzes.containsKey(quiz.getId())) {
            throw new QuizNotFoundException("Quiz-ul nu a fost gasit: " + quiz.getId());
        }
        scoreRecords.add(new ScoreRecord(quiz, student, score));
        AuditService.getInstance().logAction("grade_quiz");
    }

    public List<ScoreRecord> listScoresForStudent(Student student) {
        AuditService.getInstance().logAction("list_scores_for_student");
        return scoreRecords.stream()
                .filter(record -> record.getStudent().equals(student))
                .collect(Collectors.toList());
    }
}
