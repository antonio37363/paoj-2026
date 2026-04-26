package com.pao.proiect.fooddelivery.model;

public class Angajat extends Persoana
{
    protected double salariu;

    public Angajat(int id, String nume, String telefon, double salariu)
    {
        super(id, nume, telefon);
        this.salariu = salariu;
    }

    public double getSalariu() {
        return salariu;
    }

    public void setSalariu(double salariu) {
        this.salariu = salariu;
    }

    @Override
    public String getRol() {
        return "Angajat";
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", salariu=" + salariu +
                '}';
    }
}