package com.pao.project.magazin.model;

public class LinieComanda {
    private final Produs produs;
    private final int cantitate;

    public LinieComanda(Produs produs, int cantitate) {
        this.produs = produs;
        this.cantitate = cantitate;
    }

    public Produs getProdus() { return produs; }
    public int getCantitate() { return cantitate; }
    public double getSubtotal() { return produs.getPret() * cantitate; }
}
