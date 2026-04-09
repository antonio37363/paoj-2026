package com.pao.laboratory06.exercise2;

import java.util.Scanner;
import java.util.Locale;

public class SRLColaborator extends Colaborator implements PersoanaJuridica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.printf(Locale.US, "%s: %s, venit net anual: %.2f lei%n",
                tipContract(), getNumeComplet(), calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return "SRL";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitBrutLunar - cheltuieliLunare) * 12.0 * 0.84;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.SRL;
    }
}