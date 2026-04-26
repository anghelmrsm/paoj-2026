package com.pao.project.elearning.model;

public class ScoreRecord {
    private final Quiz quiz;
    private final Student student;
    private final int score;

    public ScoreRecord(Quiz quiz, Student student, int score) {
        this.quiz = quiz;
        this.student = student;
        this.score = score;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Student getStudent() {
        return student;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("ScoreRecord[student=%s, quiz=%s, score=%d]",
                student.getName(), quiz.getTitle(), score);
    }
}
