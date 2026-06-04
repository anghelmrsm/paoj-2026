package com.pao.project.magazin.model;

import java.util.Objects;

public class Produs implements Comparable<Produs> {
    private final CodProdus cod;
    private final String nume;
    private final double pret;
    private final int stoc;
    private final Categorie categorie;
    private final Furnizor furnizor;

    public Produs(CodProdus cod, String nume, double pret, int stoc, Categorie categorie, Furnizor furnizor) {
        this.cod = cod;
        this.nume = nume;
        this.pret = pret;
        this.stoc = stoc;
        this.categorie = categorie;
        this.furnizor = furnizor;
    }

    public CodProdus getCod() { return cod; }
    public String getNume() { return nume; }
    public double getPret() { return pret; }
    public int getStoc() { return stoc; }
    public Categorie getCategorie() { return categorie; }
    public Furnizor getFurnizor() { return furnizor; }

    @Override
    public int compareTo(Produs other) {
        int categorieCompare = categorie.getNume().compareTo(other.categorie.getNume());
        return categorieCompare != 0 ? categorieCompare : nume.compareTo(other.nume);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Produs && Objects.equals(cod, ((Produs) o).cod);
    }

    @Override
    public int hashCode() { return Objects.hash(cod); }

    @Override
    public String toString() {
        return String.format("Produs[cod=%s, nume=%s, pret=%.2f, stoc=%d, categorie=%s]",
                cod, nume, pret, stoc, categorie.getNume());
    }
}
