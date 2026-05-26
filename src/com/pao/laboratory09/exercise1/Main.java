package com.pao.laboratory09.exercise1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        List<Tranzactie> transactions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Tranzactie transaction = new Tranzactie(
                    scanner.nextInt(), scanner.nextDouble(), scanner.next(), scanner.next(),
                    scanner.next(), TipTranzactie.valueOf(scanner.next()));
            transaction.setNote("procesat");
            transactions.add(transaction);
        }
        File file = new File(OUTPUT_FILE);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(transactions);
        }
        List<Tranzactie> restored;
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<Tranzactie> value = (List<Tranzactie>) input.readObject();
            restored = value;
        }
        while (scanner.hasNext()) {
            String command = scanner.next();
            if ("LIST".equals(command)) {
                restored.forEach(System.out::println);
            } else if ("FILTER".equals(command)) {
                String month = scanner.next();
                boolean found = false;
                for (Tranzactie transaction : restored) {
                    if (transaction.getData().startsWith(month)) {
                        System.out.println(transaction);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("Niciun rezultat.");
                }
            } else if ("NOTE".equals(command)) {
                int id = scanner.nextInt();
                Tranzactie transaction = restored.stream()
                        .filter(value -> value.getId() == id).findFirst().orElse(null);
                System.out.println("NOTE[" + id + "]: "
                        + (transaction == null ? "not found" : transaction.getNote()));
            }
        }
    }
}
