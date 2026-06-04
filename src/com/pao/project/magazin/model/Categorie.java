package com.pao.project.magazin.model;

public class Categorie {
    private final int id;
    private final String nume;

    public Categorie(int id, String nume) {
        this.id = id;
        this.nume = nume;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }

    @Override
    public String toString() { return "Categorie[id=" + id + ", nume=" + nume + "]"; }
}
