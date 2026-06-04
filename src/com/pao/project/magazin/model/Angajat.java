package com.pao.project.magazin.model;

public class Angajat extends Persoana {
    private final double salariu;

    public Angajat(int id, String nume, String email, double salariu) {
        super(id, nume, email);
        this.salariu = salariu;
    }

    public double getSalariu() { return salariu; }

    @Override
    public String getRol() { return "Angajat"; }
}
