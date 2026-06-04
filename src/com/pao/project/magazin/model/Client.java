package com.pao.project.magazin.model;

public class Client extends Persoana {
    private final int puncteFidelitate;

    public Client(int id, String nume, String email, int puncteFidelitate) {
        super(id, nume, email);
        this.puncteFidelitate = puncteFidelitate;
    }

    public int getPuncteFidelitate() { return puncteFidelitate; }

    @Override
    public String getRol() { return "Client"; }
}
