package com.pao.project.elearning.model;

public class Quiz {
    private final String id;
    private final String title;
    private final Course course;
    private final int maxScore;

    public Quiz(String id, String title, Course course, int maxScore) {
        this.id = id;
        this.title = title;
        this.course = course;
        this.maxScore = maxScore;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Course getCourse() {
        return course;
    }

    public int getMaxScore() {
        return maxScore;
    }

    @Override
    public String toString() {
        return String.format("Quiz[id=%s, title=%s, course=%s, maxScore=%d]",
                id, title, course.getTitle(), maxScore);
    }
}
