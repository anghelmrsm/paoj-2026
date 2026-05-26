package com.pao.laboratory09.exercise3;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger IDS = new AtomicInteger();

    private static final class Tranzactie {
        private final int id;
        private final double suma;
        private final LocalDate data;

        private Tranzactie(int id, double suma, LocalDate data) {
            this.id = id;
            this.suma = suma;
            this.data = data;
        }
    }

    private static final class CoadaTranzactii {
        private final Queue<Tranzactie> queue = new ArrayDeque<>();
        private final int capacity;

        private CoadaTranzactii(int capacity) {
            this.capacity = capacity;
        }

        synchronized void adauga(Tranzactie transaction, String producer) throws InterruptedException {
            while (queue.size() == capacity) {
                System.out.println("[" + producer + "] astept loc...");
                wait();
            }
            queue.add(transaction);
            notifyAll();
        }

        synchronized Tranzactie extrage(ProcessorThread processor) throws InterruptedException {
            while (queue.isEmpty() && processor.activ) {
                wait();
            }
            if (queue.isEmpty()) {
                return null;
            }
            Tranzactie transaction = queue.remove();
            notifyAll();
            return transaction;
        }

        synchronized void signalStop() {
            notifyAll();
        }
    }

    private static final class ATMThread extends Thread {
        private final int atmId;
        private final CoadaTranzactii queue;

        private ATMThread(int atmId, CoadaTranzactii queue) {
            super("ATM-" + atmId);
            this.atmId = atmId;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 4; i++) {
                Tranzactie transaction = new Tranzactie(IDS.incrementAndGet(),
                        atmId * 100.0 + i * 10, LocalDate.of(2026, 5, atmId + i));
                try {
                    queue.adauga(transaction, getName());
                    System.out.printf("[%s] trimite: Tranzactie #%d %.2f RON%n",
                            getName(), transaction.id, transaction.suma);
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private static final class ProcessorThread implements Runnable {
        private final CoadaTranzactii queue;
        private volatile boolean activ = true;
        private int processed;

        private ProcessorThread(CoadaTranzactii queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (activ || processed < 12) {
                    Tranzactie transaction = queue.extrage(this);
                    if (transaction == null) {
                        break;
                    }
                    Thread.sleep(80);
                    processed++;
                    System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n",
                            transaction.id, transaction.suma, transaction.data);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii queue = new CoadaTranzactii(5);
        ProcessorThread processor = new ProcessorThread(queue);
        Thread consumer = new Thread(processor, "Processor");
        ATMThread atm1 = new ATMThread(1, queue);
        ATMThread atm2 = new ATMThread(2, queue);
        ATMThread atm3 = new ATMThread(3, queue);
        consumer.start();
        atm1.start();
        atm2.start();
        atm3.start();
        atm1.join();
        atm2.join();
        atm3.join();
        processor.activ = false;
        queue.signalStop();
        consumer.join();
        System.out.println("Toate tranzactiile procesate. Total: " + processor.processed);
    }
}
