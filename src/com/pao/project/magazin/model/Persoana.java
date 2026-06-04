package com.pao.project.magazin.model;

public abstract class Persoana {
    private final int id;
    private final String nume;
    private final String email;

    protected Persoana(int id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getEmail() { return email; }
    public abstract String getRol();

    @Override
    public String toString() {
        return getRol() + "[id=" + id + ", nume=" + nume + ", email=" + email + "]";
    }
}
