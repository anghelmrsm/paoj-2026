package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    private List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon) {
        super(nume, prenume, telefon);
        this.sold = 0.0;
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("User sau parola invalide");
        }
        System.out.println("Persoana juridica " + nume + " autentificata cu user: " + user);
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (sold >= suma) {
            sold -= suma;
            return true;
        }
        return false;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) return false;
        if (telefon == null || telefon.isEmpty()) return false;
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }

    public void setSold(double sold) {
        this.sold = sold;
    }
}