package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        super(TipColaborator.CIM);
    }

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        String bonusStr = in.next();
        bonus = "DA".equals(bonusStr);
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            venitNet *= 1.1;
        }
        return venitNet;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }
}