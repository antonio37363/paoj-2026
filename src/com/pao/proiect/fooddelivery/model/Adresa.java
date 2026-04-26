package com.pao.proiect.fooddelivery.model;

public final class Adresa
{
    private final String oras;
    private final String strada;
    private final String numar;

    public Adresa(String oras, String strada, String numar)
    {
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
    }

    public String getOras()
    {
        return oras;
    }

    public String getStrada()
    {
        return strada;
    }

    public String getNumar()
    {
        return numar;
    }

    @Override
    public String toString()
    {
        return strada + " " + numar + ", " + oras;
    }

}