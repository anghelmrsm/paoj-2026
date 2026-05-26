package com.pao.laboratory10.exercise1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> queue = new LinkedList<>();
        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "ENQUEUE":
                    queue.addLast(readTransaction(scanner));
                    break;
                case "PUSH":
                    queue.addFirst(readTransaction(scanner));
                    break;
                case "DEQUEUE":
                    printRemoved(queue, "Procesat: ");
                    break;
                case "POP":
                    printRemoved(queue, "Extras: ");
                    break;
                case "REMOVE_DEBIT":
                    int debits = removeMatching(queue, transaction -> transaction.getTip() == TipTranzactie.DEBIT);
                    System.out.println("Eliminat " + debits + " tranzactii DEBIT.");
                    break;
                case "REMOVE_BELOW":
                    double threshold = scanner.nextDouble();
                    int removed = removeMatching(queue, transaction -> transaction.getSuma() < threshold);
                    System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.%n", removed, threshold);
                    break;
                case "PRINT":
                    queue.forEach(System.out::println);
                    break;
                case "SIZE":
                    System.out.println("Dimensiune coada: " + queue.size());
                    break;
                default:
                    break;
            }
        }
    }

    private static Tranzactie readTransaction(Scanner scanner) {
        return new Tranzactie(scanner.nextInt(), scanner.nextDouble(), scanner.next(),
                TipTranzactie.valueOf(scanner.next()));
    }

    private static void printRemoved(LinkedList<Tranzactie> queue, String prefix) {
        if (queue.isEmpty()) {
            System.out.println("Coada goala.");
        } else {
            System.out.println(prefix + queue.removeFirst());
        }
    }

    private static int removeMatching(LinkedList<Tranzactie> queue,
                                      java.util.function.Predicate<Tranzactie> condition) {
        int removed = 0;
        Iterator<Tranzactie> iterator = queue.iterator();
        while (iterator.hasNext()) {
            if (condition.test(iterator.next())) {
                iterator.remove();
                removed++;
            }
        }
        return removed;
    }
}
