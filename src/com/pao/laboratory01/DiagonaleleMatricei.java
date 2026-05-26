package com.pao.laboratory01;

import java.util.Scanner;

/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

public class DiagonaleleMatricei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double[][] matrice = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrice[i][j] = scanner.nextDouble();
            }
        }

        System.out.println("Matricea:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%.2f ", matrice[i][j]);
            }
            System.out.println();
        }

        double sumaPrincipala = 0;
        double produsSecundara = 1;
        for (int i = 0; i < n; i++) {
            sumaPrincipala += matrice[i][i];
            produsSecundara *= matrice[i][n - 1 - i];
        }

        System.out.printf("Suma diagonalei principale: %.2f\n", sumaPrincipala);
        System.out.printf("Produs diagonalei secundare: %.2f\n", produsSecundara);
    }
}
