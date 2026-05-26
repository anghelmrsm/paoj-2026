package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listează după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");
            int optiune = scanner.nextInt();
            scanner.nextLine();

            if (optiune == 0) {
                break;
            }

            switch (optiune) {
                case 1 -> {
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();
                    System.out.print("Departament: ");
                    String departament = scanner.nextLine();
                    System.out.print("Locație: ");
                    String locatie = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    scanner.nextLine();
                    service.addAngajat(new Angajat(nume, new Departament(departament, locatie), salariu));
                }
                case 2 -> service.listBySalary();
                case 3 -> {
                    System.out.print("Nume departament: ");
                    String dept = scanner.nextLine();
                    service.findByDepartament(dept);
                }
                default -> System.out.println("Opțiune invalidă.");
            }
        }

        System.out.println("La revedere!");
    }
}
