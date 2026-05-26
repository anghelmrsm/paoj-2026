package com.pao.laboratory11.exercise3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;

public class Main {
    private static final class Transaction {
        private final int id;
        private final double amount;
        private final String country;
        private final String channel;

        private Transaction(int id, double amount, String country, String channel) {
            this.id = id; this.amount = amount; this.country = country; this.channel = channel;
        }
    }

    private static final class Snapshot {
        private final Map<String, Long> byCountry;
        private final Map<String, Long> byChannel;
        private final double total;
        private final List<Transaction> top;

        private Snapshot(Map<String, Long> byCountry, Map<String, Long> byChannel,
                         double total, List<Transaction> top) {
            this.byCountry = Collections.unmodifiableMap(new HashMap<>(byCountry));
            this.byChannel = Collections.unmodifiableMap(new HashMap<>(byChannel));
            this.total = total;
            this.top = Collections.unmodifiableList(new ArrayList<>(top));
        }
    }

    private static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Aggregate {
            final Map<String, Long> byCountry = new HashMap<>();
            final Map<String, Long> byChannel = new HashMap<>();
            final List<Transaction> all = new ArrayList<>();
            double total;
        }
        return Collector.of(Aggregate::new,
                (a, t) -> {
                    a.byCountry.merge(t.country, 1L, Long::sum);
                    a.byChannel.merge(t.channel, 1L, Long::sum);
                    a.total += t.amount;
                    a.all.add(t);
                },
                (a, b) -> {
                    b.byCountry.forEach((key, value) -> a.byCountry.merge(key, value, Long::sum));
                    b.byChannel.forEach((key, value) -> a.byChannel.merge(key, value, Long::sum));
                    a.total += b.total;
                    a.all.addAll(b.all);
                    return a;
                },
                a -> {
                    a.all.sort(Comparator.comparingDouble((Transaction t) -> t.amount).reversed()
                            .thenComparingInt(t -> t.id));
                    return new Snapshot(a.byCountry, a.byChannel, a.total,
                            a.all.subList(0, Math.min(topN, a.all.size())));
                });
    }

    public static void main(String[] args) {
        List<Transaction> data = Arrays.asList(
                new Transaction(1, 800, "RO", "APP"), new Transaction(2, 1200, "RO", "WEB"),
                new Transaction(3, 350, "DE", "POS"), new Transaction(4, 1200, "DE", "WEB"),
                new Transaction(5, 500, "RO", "APP"));
        Snapshot snapshot = data.stream().collect(toSnapshot(3));
        System.out.printf(Locale.US, "Total snapshot: %.2f RON%n", snapshot.total);
        System.out.println("Top tranzactii:");
        snapshot.top.forEach(t -> System.out.printf(Locale.US, "[%d] %.2f RON%n", t.id, t.amount));
        System.out.println("Tari: " + snapshot.byCountry);
        System.out.println("Canale: " + snapshot.byChannel);
    }
}
