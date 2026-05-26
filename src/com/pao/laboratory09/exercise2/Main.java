package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) {
            return;
        }
        int n = scanner.nextInt();
        File file = new File(OUTPUT_FILE);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < n; i++) {
                writeRecord(output, scanner.nextInt(), scanner.nextDouble(), scanner.next(),
                        TipTranzactie.valueOf(scanner.next()));
            }
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();
                if ("READ".equals(command)) {
                    printRecord(raf, scanner.nextInt());
                } else if ("UPDATE".equals(command)) {
                    int idx = scanner.nextInt();
                    String status = scanner.next();
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusByte(status));
                    System.out.println("Updated [" + idx + "]: " + status);
                } else if ("PRINT_ALL".equals(command)) {
                    for (int i = 0; i < n; i++) {
                        printRecord(raf, i);
                    }
                }
            }
        }
    }

    private static void writeRecord(DataOutputStream output, int id, double amount, String date,
                                    TipTranzactie type) throws IOException {
        ByteBuffer record = ByteBuffer.allocate(RECORD_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        record.putInt(id);
        record.putDouble(amount);
        record.put(String.format("%-10s", date).substring(0, 10).getBytes(StandardCharsets.US_ASCII));
        record.put((byte) (type == TipTranzactie.CREDIT ? 0 : 1));
        record.put((byte) 0);
        output.write(record.array());
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        byte[] raw = new byte[RECORD_SIZE];
        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(raw);
        ByteBuffer record = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN);
        int id = record.getInt();
        double amount = record.getDouble();
        byte[] dateRaw = new byte[10];
        record.get(dateRaw);
        String date = new String(dateRaw, StandardCharsets.US_ASCII).trim();
        String type = record.get() == 0 ? "CREDIT" : "DEBIT";
        String status = statusName(record.get());
        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, date, type, amount, status);
    }

    private static byte statusByte(String status) {
        return (byte) ("PROCESSED".equals(status) ? 1 : "REJECTED".equals(status) ? 2 : 0);
    }

    private static String statusName(byte status) {
        return status == 1 ? "PROCESSED" : status == 2 ? "REJECTED" : "PENDING";
    }
}
