package com.pao.laboratory01;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Exercitiul 1
 *
 * Cititi de la tastatura un sir cu n elemente intregi.
 *
 * 1. Afisati elementele sirului in doua modalitati.
 * 2. Afisati media aritmetica a elementelor sirului.
 *
 */

public class MediaAritmetica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] valori = new int[n];
        int suma = 0;

        for (int i = 0; i < n; i++) {
            valori[i] = scanner.nextInt();
            suma += valori[i];
        }

        System.out.println("Sirul original: " + Arrays.toString(valori));
        System.out.print("Sirul pe linii: ");
        for (int valoare : valori) {
            System.out.print(valoare + " ");
        }
        System.out.println();

        double medie = n == 0 ? 0 : (double) suma / n;
        System.out.printf("Media aritmetica: %.2f\n", medie);
    }
}
