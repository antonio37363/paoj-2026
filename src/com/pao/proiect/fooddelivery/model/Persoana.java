package com.pao.proiect.fooddelivery.model;

public abstract class Persoana
{
    protected int id;
    protected String nume;
    protected String telefon;

    public Persoana(int id, String nume, String telefon)
    {
        this.id = id;
        this.nume = nume;
        this.telefon = telefon;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public abstract String getRol();

    @Override
    public String toString() {
        return "Persoana{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }
}