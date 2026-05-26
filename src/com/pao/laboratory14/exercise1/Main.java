package com.pao.laboratory14.exercise1;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        java.util.List<Bilet> tickets = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            tickets.add(new Bilet(scanner.nextInt(), scanner.next(), TipBilet.valueOf(scanner.next()),
                    scanner.nextDouble()));
        }
        RaportVanzari report = tickets.stream().collect(toRaportVanzari());
        String command = scanner.hasNext() ? scanner.next() : "RAPORT_SIMPLU";
        printTypes(report);
        if ("RAPORT_COMPLET".equals(command)) {
            System.out.println("---");
            System.out.printf(Locale.US, "Total: %.2f RON%n", report.getTotalGlobal());
            System.out.printf(Locale.US, "Medie: %.2f RON%n", report.getMedieGlobala());
            System.out.println("Cel mai popular: " + report.getTipCelMaiPopular());
        }
    }

    public static Collector<Bilet, ?, RaportVanzari> toRaportVanzari() {
        class Accumulator {
            final Map<TipBilet, Long> counts = new EnumMap<>(TipBilet.class);
            final Map<TipBilet, Double> totals = new EnumMap<>(TipBilet.class);
            long count;
            double total;
        }
        return Collector.of(Accumulator::new,
                (acc, ticket) -> {
                    acc.counts.merge(ticket.getTip(), 1L, Long::sum);
                    acc.totals.merge(ticket.getTip(), ticket.getPret(), Double::sum);
                    acc.count++;
                    acc.total += ticket.getPret();
                },
                (left, right) -> {
                    right.counts.forEach((type, count) -> left.counts.merge(type, count, Long::sum));
                    right.totals.forEach((type, total) -> left.totals.merge(type, total, Double::sum));
                    left.count += right.count;
                    left.total += right.total;
                    return left;
                },
                acc -> {
                    TipBilet popular = null;
                    long maximum = -1;
                    for (TipBilet type : TipBilet.values()) {
                        long count = acc.counts.getOrDefault(type, 0L);
                        if (count > maximum) {
                            popular = type;
                            maximum = count;
                        }
                    }
                    return new RaportVanzari(acc.counts, acc.totals, acc.total,
                            acc.count == 0 ? 0 : acc.total / acc.count, popular);
                });
    }

    private static void printTypes(RaportVanzari report) {
        for (TipBilet type : TipBilet.values()) {
            if (report.getNumarPerTip().containsKey(type)) {
                System.out.printf(Locale.US, "%s: count=%d incasari=%.2f RON%n", type,
                        report.getNumarPerTip().get(type), report.getIncasariPerTip().get(type));
            }
        }
    }
}
