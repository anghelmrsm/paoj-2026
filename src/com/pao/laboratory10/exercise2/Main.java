package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    private static final Comparator<Tranzactie> BY_AMOUNT =
            Comparator.comparingDouble(Tranzactie::getSuma);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        List<Tranzactie> transactions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            transactions.add(new Tranzactie(scanner.nextInt(), scanner.nextDouble(), scanner.next(),
                    TipTranzactie.valueOf(scanner.next())));
        }
        while (scanner.hasNext()) {
            String command = scanner.next();
            switch (command) {
                case "UNIQUE_IDS":
                    Set<Integer> ids = new LinkedHashSet<>();
                    transactions.forEach(transaction -> ids.add(transaction.getId()));
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                case "MONTHLY_REPORT":
                    printMonthlyReport(transactions);
                    break;
                case "TOP":
                    int count = scanner.nextInt();
                    List<Tranzactie> top = new ArrayList<>(transactions);
                    top.sort(BY_AMOUNT.reversed());
                    int limit = Math.min(count, top.size());
                    System.out.println("Top " + count + ":");
                    top.subList(0, limit).forEach(System.out::println);
                    break;
                case "SORT_ASC":
                    transactions.sort(BY_AMOUNT);
                    transactions.forEach(System.out::println);
                    break;
                case "SORT_DESC":
                    transactions.sort(BY_AMOUNT.reversed());
                    transactions.forEach(System.out::println);
                    break;
                case "REVERSE":
                    Collections.reverse(transactions);
                    transactions.forEach(System.out::println);
                    break;
                case "MIN_MAX":
                    if (!transactions.isEmpty()) {
                        System.out.println("MIN: " + Collections.min(transactions, BY_AMOUNT));
                        System.out.println("MAX: " + Collections.max(transactions, BY_AMOUNT));
                    }
                    break;
                case "CME_DEMO":
                    try {
                        for (Tranzactie transaction : transactions) {
                            transactions.remove(transaction);
                        }
                    } catch (ConcurrentModificationException ex) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static void printMonthlyReport(List<Tranzactie> transactions) {
        TreeMap<String, double[]> totals = new TreeMap<>();
        for (Tranzactie transaction : transactions) {
            double[] amount = totals.computeIfAbsent(transaction.getData().substring(0, 7),
                    key -> new double[2]);
            amount[transaction.getTip() == TipTranzactie.CREDIT ? 0 : 1] += transaction.getSuma();
        }
        totals.forEach((month, amount) -> System.out.printf(Locale.US,
                "%s: CREDIT %.2f RON, DEBIT %.2f RON%n", month, amount[0], amount[1]));
    }
}
