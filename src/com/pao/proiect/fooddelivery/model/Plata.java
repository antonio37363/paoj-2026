package com.pao.proiect.fooddelivery.model;

public class Plata
{
    private int id;
    private double suma;
    private String metodaPlata;
    private boolean efectuata;

    public Plata(int id, double suma, String metodaPlata)
    {
        this.id = id;
        this.suma = suma;
        this.metodaPlata = metodaPlata;
        this.efectuata = false;
    }

    public int getId() {
        return id;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public String getMetodaPlata() {
        return metodaPlata;
    }

    public void setMetodaPlata(String metodaPlata) {
        this.metodaPlata = metodaPlata;
    }

    public boolean isEfectuata() {
        return efectuata;
    }

    public void setEfectuata(boolean efectuata) {
        this.efectuata = efectuata;
    }

    @Override
    public String toString() {
        return "Plata{" +
                "id=" + id +
                ", suma=" + suma +
                ", metodaPlata='" + metodaPlata + '\'' +
                ", efectuata=" + efectuata +
                '}';
    }
}