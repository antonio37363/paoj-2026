package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract double calculeazaVenitNetAnual();

    public abstract TipColaborator getTip();

    protected String getNumeComplet() {
        return nume + " " + prenume;
    }
}