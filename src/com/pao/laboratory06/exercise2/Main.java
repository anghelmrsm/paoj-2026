package com.pao.laboratory06.exercise2;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            c.citeste(in);
            colaboratori.add(c);
        }

        // Sortează descrescător după venit net anual și afișează
        colaboratori.sort((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()));
        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }

        // Colaborator cu venit net maxim
        Colaborator max = colaboratori.stream().max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual)).orElse(null);
        System.out.println("Colaborator cu venit net maxim: ");
        if (max != null) max.afiseaza();

        // Colaboratori persoane juridice (SRL)
        System.out.println("\nColaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
                .forEach(Colaborator::afiseaza);

        // Sume și număr colaboratori pe tip
        System.out.println("\nSume si numar colaboratori pe tip:");
        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
        for (TipColaborator tip : TipColaborator.values()) {
            suma.put(tip, 0.0);
            numar.put(tip, 0);
        }
        for (Colaborator c : colaboratori) {
            suma.put(c.getTip(), suma.get(c.getTip()) + c.calculeazaVenitNetAnual());
            numar.put(c.getTip(), numar.get(c.getTip()) + 1);
        }
        for (TipColaborator tip : TipColaborator.values()) {
            if (numar.get(tip) > 0) {
                System.out.printf("%s: suma = %.2f lei, numar = %d\n", tip, suma.get(tip), numar.get(tip));
            }
        }
    }
}