package com.pao.project.magazin.model;

public class Manager extends Angajat {
    private final String departament;

    public Manager(int id, String nume, String email, double salariu, String departament) {
        super(id, nume, email, salariu);
        this.departament = departament;
    }

    public String getDepartament() { return departament; }

    @Override
    public String getRol() { return "Manager"; }
}
