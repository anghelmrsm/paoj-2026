package com.pao.laboratory13.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String first = nextNonEmpty(reader);
        if (first == null) {
            return;
        }
        int commands = Integer.parseInt(first);
        ProtocolEngine engine = new ProtocolEngine();
        for (int i = 0; i < commands; i++) {
            String command = nextNonEmpty(reader);
            if (command == null) {
                return;
            }
            System.out.println(engine.execute(command));
        }
    }

    private static String nextNonEmpty(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return null;
    }
}
