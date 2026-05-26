package com.pao.laboratory05.audit;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();

        service.addAngajat(new Angajat("Ana", new Departament("IT", "București"), 6500));
        service.addAngajat(new Angajat("Mihai", new Departament("Sales", "Cluj"), 5200));
        service.addAngajat(new Angajat("Elena", new Departament("IT", "București"), 7000));

        System.out.println("\n=== Toți angajații ===");
        service.printAll();

        System.out.println("\n=== Angajați după salariu ===");
        service.listBySalary();

        System.out.println("\n=== Căutare departament IT ===");
        service.findByDepartament("IT");

        System.out.println("\n=== Audit log ===");
        service.printAuditLog();
    }
}
