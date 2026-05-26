package com.pao.laboratory10.exercise3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    private static final class Tranzactie {
        private final int id;
        private final double suma;
        private final String data;
        private final String tip;
        private final String contSursa;

        private Tranzactie(int id, double suma, String data, String tip, String contSursa) {
            this.id = id; this.suma = suma; this.data = data; this.tip = tip; this.contSursa = contSursa;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "[%d] %s %s: %.2f RON", id, data, tip, suma);
        }
    }

    public static void main(String[] args) {
        List<Tranzactie> data = Arrays.asList(
                new Tranzactie(1, 900, "2026-01-05", "CREDIT", "RO01"),
                new Tranzactie(2, 120, "2026-01-10", "DEBIT", "RO02"),
                new Tranzactie(3, 2000, "2026-01-20", "CREDIT", "RO01"),
                new Tranzactie(4, 75, "2026-02-01", "DEBIT", "RO03"),
                new Tranzactie(5, 350, "2026-02-12", "CREDIT", "RO02"),
                new Tranzactie(6, 510, "2026-02-28", "DEBIT", "RO01"),
                new Tranzactie(7, 100, "2026-03-02", "CREDIT", "RO04"),
                new Tranzactie(8, 1500, "2026-03-11", "CREDIT", "RO01"),
                new Tranzactie(9, 450, "2026-03-15", "DEBIT", "RO04"),
                new Tranzactie(10, 225, "2026-03-19", "CREDIT", "RO05"));

        System.out.println("1. Tranzactii CREDIT");
        data.stream().filter(t -> "CREDIT".equals(t.tip)).forEach(System.out::println);
        System.out.println("2. Total procesat");
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", data.stream().mapToDouble(t -> t.suma).sum());
        System.out.println("3. Total per luna");
        data.stream().collect(Collectors.groupingBy(t -> t.data.substring(0, 7), TreeMap::new,
                Collectors.summingDouble(t -> t.suma))).forEach((m, v) ->
                System.out.printf(Locale.US, "%s: %.2f RON%n", m, v));
        System.out.println("4. Top 3 tranzactii");
        data.stream().sorted(Comparator.comparingDouble((Tranzactie t) -> t.suma).reversed()).limit(3)
                .forEach(System.out::println);
        System.out.println("5. Conturi sursa unice");
        System.out.println("Conturi sursa unice: " + data.stream().map(t -> t.contSursa).distinct()
                .collect(Collectors.toList()));
        System.out.println("6. Suma medie");
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n",
                data.stream().mapToDouble(t -> t.suma).average().orElse(0));
        System.out.println("7. Extrase lunare");
        Map<String, List<Tranzactie>> groups = data.stream()
                .collect(Collectors.groupingBy(t -> t.data.substring(0, 7), TreeMap::new, Collectors.toList()));
        groups.forEach((month, list) -> System.out.printf(Locale.US,
                "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                month, list.size(), list.stream().mapToDouble(t -> t.suma).sum()));
    }
}
