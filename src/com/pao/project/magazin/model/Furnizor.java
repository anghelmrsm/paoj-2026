package com.pao.project.magazin.model;

public class Furnizor {
    private final int id;
    private final String nume;
    private final String telefon;

    public Furnizor(int id, String nume, String telefon) {
        this.id = id;
        this.nume = nume;
        this.telefon = telefon;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getTelefon() { return telefon; }

    @Override
    public String toString() { return "Furnizor[id=" + id + ", nume=" + nume + "]"; }
}
