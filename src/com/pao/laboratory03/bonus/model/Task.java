package com.pao.laboratory03.bonus.model;

import com.pao.laboratory03.bonus.enums.Priority;
import com.pao.laboratory03.bonus.enums.Status;

public class Task {
    private final String id;
    private String title;
    private Status status;
    private Priority priority;
    private String assignee;

    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.TODO;
        this.assignee = null;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public String getAssignee() { return assignee; }

    public void setStatus(Status status) { this.status = status; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    @Override
    public String toString() {
        return String.format("Task{id='%s', title='%s', priority=%s, status=%s, assignee=%s}",
                id, title, priority, status, assignee);
    }
}