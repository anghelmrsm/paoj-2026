package com.pao.laboratory14.exercise3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    private record Eveniment(String nume, int start, int end) {
        private Eveniment(String nume, String start, String end) {
            this(nume, toMinutes(start), toMinutes(end));
        }
    }

    public static void main(String[] args) {
        List<Eveniment> events = new ArrayList<>(Arrays.asList(
                new Eveniment("Keynote", "09:00", "10:00"),
                new Eveniment("Java Workshop", "09:15", "11:00"),
                new Eveniment("Cloud Talk", "09:30", "10:30"),
                new Eveniment("Lunch Panel", "10:00", "11:15"),
                new Eveniment("Security", "10:45", "12:00"),
                new Eveniment("AI Demo", "11:00", "12:30"),
                new Eveniment("Databases", "11:30", "13:00"),
                new Eveniment("Closing", "12:30", "13:30")));
        events.sort(Comparator.comparingInt(Eveniment::start));

        System.out.println("Greedy O(N^2):");
        List<Integer> roomEnds = new ArrayList<>();
        for (Eveniment event : events) {
            int room = firstAvailable(roomEnds, event.start());
            if (room == roomEnds.size()) {
                roomEnds.add(event.end());
            } else {
                roomEnds.set(room, event.end());
            }
            System.out.printf("%-18s (%s - %s) -> Sala #%d%n", event.nume(),
                    format(event.start()), format(event.end()), room + 1);
        }
        System.out.println("Sali minime: " + roomEnds.size());

        PriorityQueue<Integer> occupied = new PriorityQueue<>();
        int maximum = 0;
        for (Eveniment event : events) {
            while (!occupied.isEmpty() && occupied.peek() <= event.start()) {
                occupied.poll();
            }
            occupied.offer(event.end());
            maximum = Math.max(maximum, occupied.size());
        }
        System.out.println("PriorityQueue O(N log N): " + maximum + " sali minime.");
    }

    private static int firstAvailable(List<Integer> roomEnds, int start) {
        for (int i = 0; i < roomEnds.size(); i++) {
            if (roomEnds.get(i) <= start) {
                return i;
            }
        }
        return roomEnds.size();
    }

    private static int toMinutes(String value) {
        String[] fields = value.split(":");
        return Integer.parseInt(fields[0]) * 60 + Integer.parseInt(fields[1]);
    }

    private static String format(int value) {
        return String.format("%02d:%02d", value / 60, value % 60);
    }
}
