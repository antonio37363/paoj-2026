package com.pao.proiect.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

public class Client extends Persoana
{
    private Adresa adresa;
    private List<Comanda> istoricComenzi;

    public Client(int id, String nume, String telefon, Adresa adresa)
    {
        super(id, nume, telefon);
        this.adresa = adresa;
        this.istoricComenzi = new ArrayList<>();
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public List<Comanda> getIstoricComenzi() {
        return istoricComenzi;
    }

    public void adaugaComanda(Comanda comanda)
    {
        if (comanda != null) {
            istoricComenzi.add(comanda);
        }
    }

    @Override
    public String getRol() {
        return "Client";
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", adresa=" + adresa +
                '}';
    }
}