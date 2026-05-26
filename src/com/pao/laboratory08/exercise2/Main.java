package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();

        try {
            List<Student> studenti = readStudents();
            List<Student> filtrati = new ArrayList<>();
            for (Student student : studenti) {
                if (student.getVarsta() >= prag) {
                    filtrati.add(student);
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("rezultate.txt"))) {
                for (Student student : filtrati) {
                    writer.write(student.toString());
                    writer.newLine();
                }
            }

            System.out.println("Filtru: varsta >= " + prag);
            System.out.println("Rezultate: " + filtrati.size() + " studenti");
            System.out.println();
            for (Student student : filtrati) {
                System.out.println(student);
            }
            System.out.println();
            System.out.println("Scris in: rezultate.txt");
        } catch (IOException e) {
            System.out.println("Eroare la citire/scriere: " + e.getMessage());
        }
    }

    private static List<Student> readStudents() throws IOException {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = reader.readLine()) != null) {
                if (linie.isBlank()) continue;
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

