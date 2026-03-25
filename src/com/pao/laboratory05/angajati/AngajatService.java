package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];

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
}
