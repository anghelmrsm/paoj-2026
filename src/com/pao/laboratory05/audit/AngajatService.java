package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat angajat) {
        Angajat[] resized = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, resized, 0, angajati.length);
        resized[resized.length - 1] = angajat;
        angajati = resized;
        System.out.println("Angajat adaugat: " + angajat.getNume());
        logAction("ADD", angajat.getNume());
    }

    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);

        boolean found = false;
        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                if (!found) {
                    System.out.println("--- Angajati din " + numeDept + " ---");
                }
                System.out.println(angajat);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Niciun angajat in departamentul: " + numeDept);
        }
    }

    public void printAuditLog() {
        if (auditLog.length == 0) {
            System.out.println("Audit log gol.");
            return;
        }

        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] resized = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, resized, 0, auditLog.length);
        resized[resized.length - 1] = entry;
        auditLog = resized;
    }
}
