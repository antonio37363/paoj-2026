package com.pao.laboratory06.exercise2;

import java.util.Locale;
import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();

        bonus = false;
        if (in.hasNext("DA|NU")) {
            bonus = in.next().equals("DA");
        }
    }

    @Override
    public void afiseaza() {
        System.out.printf(Locale.US, "%s: %s, venit net anual: %.2f lei%n",
                tipContract(), getNumeComplet(), calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            net *= 1.10;
        }
        return net;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }
}