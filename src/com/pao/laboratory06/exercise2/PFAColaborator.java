package com.pao.laboratory06.exercise2;

import java.util.Scanner;
import java.util.Locale;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private static final double SALARIU_MINIM_BRUT_LUNAR = 4050.0;
    private static final double SALARIU_MINIM_BRUT_ANUAL = SALARIU_MINIM_BRUT_LUNAR * 12;

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
        return "PFA";
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetInainteTaxe = (venitBrutLunar - cheltuieliLunare) * 12.0;

        double impozit = 0.10 * venitNetInainteTaxe;

        double cass;
        if (venitNetInainteTaxe < 6 * SALARIU_MINIM_BRUT_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_BRUT_ANUAL);
        } else if (venitNetInainteTaxe <= 72 * SALARIU_MINIM_BRUT_ANUAL) {
            cass = 0.10 * venitNetInainteTaxe;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_BRUT_ANUAL);
        }

        double cas;
        if (venitNetInainteTaxe < 12 * SALARIU_MINIM_BRUT_ANUAL) {
            cas = 0.0;
        } else if (venitNetInainteTaxe <= 24 * SALARIU_MINIM_BRUT_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_BRUT_ANUAL);
        } else {
            cas = 0.25 * (24 * SALARIU_MINIM_BRUT_ANUAL);
        }

        return venitNetInainteTaxe - impozit - cass - cas;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }
}