package com.pao.laboratory04.enums;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Toate prioritatile ===");
        for (Priority priority : Priority.values()) {
            System.out.printf("%s %s (level=%d, color=%s)%n",
                    priority.getEmoji(), priority.name(), priority.getLevel(), priority.getColor());
        }

        System.out.println();
        System.out.println("=== Switch pe prioritate ===");
        Priority current = Priority.HIGH;
        switch (current) {
            case LOW:
                System.out.println("Prioritate scazuta.");
                break;
            case MEDIUM:
                System.out.println("Prioritate medie.");
                break;
            case HIGH:
                System.out.println("Atentie! Prioritate ridicata!");
                break;
            case CRITICAL:
                System.out.println("Urgent! Prioritate critica!");
                break;
        }

        System.out.println();
        System.out.println("=== valueOf ===");
        Priority fromString = Priority.valueOf("HIGH");
        System.out.println("Priority.valueOf(\"HIGH\") = " + fromString);

        System.out.println();
        System.out.println("=== Comparare enum ===");
        System.out.println("HIGH == HIGH? " + (fromString == Priority.HIGH));
        System.out.println("HIGH == LOW? " + (fromString == Priority.LOW));

        System.out.println();
        System.out.println("=== name() si ordinal() ===");
        for (Priority priority : Priority.values()) {
            System.out.printf("%s: name=%s, ordinal=%d%n",
                    priority.name(), priority.name(), priority.ordinal());
        }
    }
}
