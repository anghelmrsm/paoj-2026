package com.pao.laboratory04.bonus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private static final TaskService INSTANCE = new TaskService();

    private final Map<String, Task> tasksById = new HashMap<>();
    private final Map<Priority, List<Task>> tasksByPriority = new EnumMap<>(Priority.class);
    private final List<String> auditLog = new ArrayList<>();
    private int nextId = 1;

    private TaskService() {
        for (Priority priority : Priority.values()) {
            tasksByPriority.put(priority, new ArrayList<>());
        }
    }

    public static TaskService getInstance() {
        return INSTANCE;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);
        return addTask(id, title, priority);
    }

    public Task addTask(String id, String title, Priority priority) {
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task-ul '" + id + "' exista deja");
        }
        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public Task findTask(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost gasit");
        }
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = findTask(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " -> " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findTask(taskId);
        Status oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }
        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " -> " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.getOrDefault(priority, List.of()));
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new EnumMap<>(Status.class);
        for (Status status : Status.values()) {
            summary.put(status, 0L);
        }
        for (Task task : tasksById.values()) {
            summary.put(task.getStatus(), summary.get(task.getStatus()) + 1);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                unassigned.add(task);
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
        double total = 0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                total += task.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }
}
