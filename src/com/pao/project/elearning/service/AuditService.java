package com.pao.project.elearning.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static final AuditService INSTANCE = new AuditService();
    private static final String DEFAULT_FILE_NAME = "src/com/pao/project/elearning/audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private AuditService() {
    }

    public static AuditService getInstance() {
        return INSTANCE;
    }

    public synchronized void logAction(String action) {
        String fileName = System.getProperty("elearning.audit.file", DEFAULT_FILE_NAME);
        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(action + "," + LocalDateTime.now().format(FORMATTER));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Eroare audit: " + e.getMessage());
        }
    }
}
