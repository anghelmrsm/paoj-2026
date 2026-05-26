package com.pao.project.elearning.service;

import com.pao.project.elearning.exception.QuizNotFoundException;
import com.pao.project.elearning.model.Quiz;
import com.pao.project.elearning.model.ScoreRecord;
import com.pao.project.elearning.model.Student;
import com.pao.project.elearning.repository.QuizRepository;
import com.pao.project.elearning.repository.ScoreRecordRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuizService {
    private static final QuizService INSTANCE = new QuizService();
    private final Map<String, Quiz> quizzes = new HashMap<>();
    private final List<ScoreRecord> scoreRecords = new ArrayList<>();
    private final QuizRepository quizRepository = new QuizRepository();
    private final ScoreRecordRepository scoreRecordRepository = new ScoreRecordRepository();

    private QuizService() {
    }

    public static QuizService getInstance() {
        return INSTANCE;
    }

    public void addQuiz(Quiz quiz) {
        if (quizzes.putIfAbsent(quiz.getId(), quiz) == null) {
            quizRepository.save(quiz);
        }
        AuditService.getInstance().logAction("add_quiz");
    }

    public Quiz findQuizById(String id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            throw new QuizNotFoundException("Quiz-ul nu a fost gasit: " + id);
        }
        return quiz;
    }

    public void gradeQuiz(Quiz quiz, Student student, int score) {
        if (!quizzes.containsKey(quiz.getId())) {
            throw new QuizNotFoundException("Quiz-ul nu a fost gasit: " + quiz.getId());
        }
        ScoreRecord record = new ScoreRecord(quiz, student, score);
        scoreRecords.add(record);
        scoreRecordRepository.save(record);
        AuditService.getInstance().logAction("grade_quiz");
    }

    public List<ScoreRecord> listScoresForStudent(Student student) {
        AuditService.getInstance().logAction("list_scores_for_student");
        return scoreRecordRepository.findByStudent(student);
    }
}
