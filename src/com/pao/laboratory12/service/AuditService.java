package com.pao.laboratory12.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public final class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private final ReentrantLock lock = new ReentrantLock();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {}

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String action) {
        lock.lock();
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.println(action + "," + LocalDateTime.now().format(formatter));
        } catch (IOException ex) {
            System.err.println("[AUDIT] " + ex.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
