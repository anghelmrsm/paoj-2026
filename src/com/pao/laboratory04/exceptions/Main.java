package com.pao.laboratory04.exceptions;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== a) Unchecked - NullPointerException ===");
        try {
            riskyMethod();
        } catch (NullPointerException e) {
            System.out.println("Prins: " + e.getMessage());
        } finally {
            System.out.println("Finally se executa mereu!");
        }

        System.out.println();
        System.out.println("=== b) Custom exceptions ===");
        try {
            validateAge(-5);
        } catch (InvalidAgeException e) {
            System.out.println("InvalidAgeException: " + e.getMessage());
        }

        List<String> names = new ArrayList<>();
        names.add("Ana");
        try {
            addToList(names, "Ana");
        } catch (DuplicateEntryException e) {
            System.out.println("DuplicateEntryException: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== c) Multi-catch ===");
        try {
            validateAge(200);
            addToList(names, "Ana");
        } catch (InvalidAgeException | DuplicateEntryException e) {
            System.out.println("Exceptie prinsa: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== d) Catch ordering (specific -> general) ===");
        try {
            validateAge(-1);
        } catch (InvalidAgeException e) {
            System.out.println("InvalidAgeException prinsa specific: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("RuntimeException prinsa general: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== e) Throw vs throws ===");
        try {
            process(999);
        } catch (InvalidAgeException e) {
            System.out.println("Metoda process() a aruncat: " + e.getMessage());
        }
    }

    private static void riskyMethod() {
        String s = null;
        s.length();
    }

    private static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Varsta " + age + " nu este valida (0-150)");
        }
    }

    private static void addToList(List<String> list, String name) {
        if (list.contains(name)) {
            throw new DuplicateEntryException("'" + name + "' exista deja in lista");
        }
        list.add(name);
    }

    private static void process(int age) throws InvalidAgeException {
        validateAge(age);
    }
}
