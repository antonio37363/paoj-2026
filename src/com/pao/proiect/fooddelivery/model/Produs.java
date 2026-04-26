package com.pao.proiect.fooddelivery.model;

import java.util.Objects;

public class Produs implements Comparable<Produs>
{
    private int id;
    private String nume;
    private double pret;
    private String categorie;

    public Produs(int id, String nume, double pret, String categorie)
    {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.categorie = categorie;
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

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public int compareTo(Produs other)
    {
        int rezultat = this.nume.compareToIgnoreCase(other.nume);

        if (rezultat == 0) {
            return Integer.compare(this.id, other.id);
        }

        return rezultat;
    }

    @Override
    public String toString()
    {
        return "Produs{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", pret=" + pret +
                ", categorie='" + categorie + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Produs)) return false;
        Produs produs = (Produs) o;
        return id == produs.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}