package com.pao.laboratory03.bonus.service;

import com.pao.laboratory03.bonus.enums.Priority;
import com.pao.laboratory03.bonus.enums.Status;
import com.pao.laboratory03.bonus.exception.DuplicateTaskException;
import com.pao.laboratory03.bonus.exception.InvalidTransitionException;
import com.pao.laboratory03.bonus.exception.TaskNotFoundException;
import com.pao.laboratory03.bonus.model.Task;

import java.util.*;


public class TaskService {
    private static TaskService instance;

    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int nextId = 1;

    private TaskService() {
        tasksById = new HashMap<>();
        tasksByPriority = new HashMap<>();
        for (Priority p : Priority.values()) {
            tasksByPriority.put(p, new ArrayList<>());
        }
        auditLog = new ArrayList<>();
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);

        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task-ul cu ID " + id + " există deja.");
        }

        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);

        auditLog.add(String.format("[ADD] %s: '%s' (%s)", id, title, priority));
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = getTaskById(taskId);
        task.setAssignee(assignee);
        auditLog.add(String.format("[ASSIGN] %s → %s", taskId, assignee));
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = getTaskById(taskId);
        Status oldStatus = task.getStatus();

        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add(String.format("[STATUS] %s: %s → %s", taskId, oldStatus, newStatus));
    }

    private Task getTaskById(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit.");
        }
        return task;
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.get(priority));
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();
        for (Status s : Status.values()) {
            summary.put(s, 0L);
        }
        for (Task t : tasksById.values()) {
            summary.put(t.getStatus(), summary.get(t.getStatus()) + 1);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task t : tasksById.values()) {
            if (t.getAssignee() == null) {
                unassigned.add(t);
            }
        }
        return unassigned;
    }

    public void printAuditLog() {
        for (String log : auditLog) {
            System.out.println(log);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double totalScore = 0;
        for (Task t : tasksById.values()) {
            if (t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED) {
                totalScore += t.getPriority().calculateScore(baseDays);
            }
        }
        return totalScore;
    }
}