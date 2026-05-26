package com.pao.laboratory04.bonus;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TaskService service = TaskService.getInstance();

        System.out.println("=== Adaugare task-uri ===");
        Task t1 = service.addTask("Fix login bug", Priority.CRITICAL);
        Task t2 = service.addTask("Add dark mode", Priority.LOW);
        Task t3 = service.addTask("Update docs", Priority.MEDIUM);
        Task t4 = service.addTask("Fix memory leak", Priority.HIGH);
        Task t5 = service.addTask("Refactor DB layer", Priority.HIGH);
        System.out.println("Adaugat: " + t1);
        System.out.println("Adaugat: " + t2);
        System.out.println("Adaugat: " + t3);
        System.out.println("Adaugat: " + t4);
        System.out.println("Adaugat: " + t5);

        System.out.println();
        System.out.println("=== Asignare ===");
        service.assignTask("T001", "Ana");
        service.assignTask("T003", "Mihai");
        service.assignTask("T004", "Elena");
        System.out.println("T001 -> Ana");
        System.out.println("T003 -> Mihai");
        System.out.println("T004 -> Elena");

        System.out.println();
        System.out.println("=== Schimbari status ===");
        changeStatusAndPrint(service, "T001", Status.IN_PROGRESS);
        changeStatusAndPrint(service, "T001", Status.DONE);
        changeStatusAndPrint(service, "T003", Status.IN_PROGRESS);
        try {
            service.changeStatus("T001", Status.TODO);
        } catch (InvalidTransitionException e) {
            System.out.println("T001: DONE -> TODO -> InvalidTransitionException: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Task-uri HIGH ===");
        for (Task task : service.getTasksByPriority(Priority.HIGH)) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("=== Sumar status ===");
        for (Map.Entry<Status, Long> entry : service.getStatusSummary().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println();
        System.out.println("=== Task-uri neasignate ===");
        for (Task task : service.getUnassignedTasks()) {
            System.out.println(task.getId() + ": " + task.getTitle());
        }

        System.out.println();
        System.out.println("=== Scor urgenta (baseDays=5) ===");
        System.out.println("Total: " + service.getTotalUrgencyScore(5));

        System.out.println();
        System.out.println("=== Audit Log ===");
        service.printAuditLog();

        System.out.println();
        System.out.println("=== Exceptii ===");
        try {
            service.addTask("T001", "Duplicate task", Priority.LOW);
        } catch (DuplicateTaskException e) {
            System.out.println("DuplicateTaskException: " + e.getMessage());
        }

        try {
            service.findTask("T999");
        } catch (TaskNotFoundException e) {
            System.out.println("TaskNotFoundException: " + e.getMessage());
        }
    }

    private static void changeStatusAndPrint(TaskService service, String taskId, Status newStatus) {
        Status oldStatus = service.findTask(taskId).getStatus();
        service.changeStatus(taskId, newStatus);
        System.out.println(taskId + ": " + oldStatus + " -> " + newStatus + " OK");
    }
}
