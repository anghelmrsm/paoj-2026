package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Creează și sortează un array de Inginer (natural și cu comparatorul de salariu)
        Inginer[] ingineri = {
            new Inginer("Zoe", "Popescu", "0712345678", 8000),
            new Inginer("Ana", "Ionescu", "0712345679", 7000),
            new Inginer("Maria", "Georgescu", "0712345680", 9000)
        };

        // Setează solduri pentru demonstrație
        ingineri[0].setSold(10000);
        ingineri[1].setSold(5000);
        ingineri[2].setSold(15000);

        System.out.println("Sortare naturală (după nume):");
        Arrays.sort(ingineri);
        for (Inginer i : ingineri) {
            System.out.println(i.getNume() + " " + i.getPrenume() + " - salariu: " + i.getSalariu());
        }

        System.out.println("\nSortare după salariu descrescător:");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer i : ingineri) {
            System.out.println(i.getNume() + " " + i.getPrenume() + " - salariu: " + i.getSalariu());
        }

        // Demonstrează accesul la un Inginer doar prin referința de tip PlataOnline
        PlataOnline po = ingineri[0];
        po.autentificare("user1", "pass1");
        System.out.println("Sold: " + po.consultareSold());
        System.out.println("Plată reușită: " + po.efectuarePlata(2000));

        // Demonstrează accesul la o PersoanaJuridica prin referința de tip PlataOnlineSMS
        PersoanaJuridica pj = new PersoanaJuridica("SRL", "Tech", "0712345681");
        pj.setSold(20000);
        PlataOnlineSMS pos = pj;
        pos.autentificare("user2", "pass2");
        System.out.println("Trimis SMS: " + pos.trimiteSMS("Confirmare plată"));
        System.out.println("SMS-uri trimise: " + pj.getSmsTrimise());

        // Afișează o constantă din enum
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());

        // Demonstrează tratarea cazurilor de eroare
        try {
            po.autentificare(null, "pass");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        // Trimitere SMS fără telefon
        PersoanaJuridica pjFaraTel = new PersoanaJuridica("SRL2", "NoTel", null);
        System.out.println("Trimis SMS fără telefon: " + pjFaraTel.trimiteSMS("Test"));

        // Apel trimiteSMS pe Inginer (care nu implementează PlataOnlineSMS)
        try {
            ((PlataOnlineSMS) po).trimiteSMS("Test"); // Ar trebui să fie UnsupportedOperationException, dar Inginer nu implementează
        } catch (ClassCastException e) {
            System.out.println("Eroare cast: Inginer nu are SMS");
        }
    }
}
