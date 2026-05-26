package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Calea catre fisierul cu date - relativa la radacina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String commandLine = scanner.nextLine().trim();

        try {
            List<Student> studenti = readStudents();
            if (commandLine.equals("PRINT")) {
                studenti.forEach(System.out::println);
                return;
            }

            String[] parts = commandLine.split(" ", 2);
            if (parts.length != 2) {
                System.out.println("Comanda invalida");
                return;
            }

            String action = parts[0];
            String nume = parts[1];
            Student original = studenti.stream()
                    .filter(s -> s.getNume().equals(nume))
                    .findFirst()
                    .orElse(null);

            if (original == null) {
                System.out.println("Studentul nu a fost gasit: " + nume);
                return;
            }

            switch (action) {
                case "SHALLOW" -> {
                    Student clona = original.shallowClone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + original);
                    System.out.println("Clona: " + clona);
                }
                case "DEEP" -> {
                    Student clona = (Student) original.clone();
                    clona.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + original);
                    System.out.println("Clona: " + clona);
                }
                default -> System.out.println("Comanda necunoscuta: " + action);
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului: " + e.getMessage());
        } catch (CloneNotSupportedException e) {
            System.out.println("Clonare nereusita: " + e.getMessage());
        }
    }

    private static List<Student> readStudents() throws IOException {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = reader.readLine()) != null) {
                if (linie.isBlank()) {
                    continue;
                }
                String[] parts = linie.split(",");
                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return studenti;
    }
}
