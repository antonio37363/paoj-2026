package com.pao.proiect.fooddelivery.model;

import java.util.Objects;

public class Restaurant
{
    private int id;
    private String nume;
    private Adresa adresa;
    private Meniu meniu;

    public Restaurant(int id, String nume, Adresa adresa)
    {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.meniu = new Meniu();
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

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public Meniu getMeniu() {
        return meniu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", adresa=" + adresa +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}