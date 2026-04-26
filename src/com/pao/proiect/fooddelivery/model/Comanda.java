package com.pao.proiect.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

public class Comanda
{
    private int id;
    private Client client;
    private Restaurant restaurant;
    private List<Produs> produse;
    private Sofer sofer;
    private StatusComanda status;
    private Plata plata;

    public Comanda(int id, Client client, Restaurant restaurant)
    {
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.produse = new ArrayList<>();
        this.status = StatusComanda.PLASATA;
        this.sofer = null;
        this.plata = null;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<Produs> getProduse() {
        return produse;
    }

    public Sofer getSofer() {
        return sofer;
    }

    public StatusComanda getStatus() {
        return status;
    }

    public Plata getPlata() {
        return plata;
    }

    public void adaugaProdus(Produs produs)
    {
        if (produs != null) {
            produse.add(produs);
        }
    }

    public void atribuieSofer(Sofer sofer) {
        this.sofer = sofer;
    }

    public void schimbaStatus(StatusComanda status) {
        this.status = status;
    }

    public double calculeazaTotal()
    {
        double total = 0;

        for (Produs produs : produse)
        {
            total = total + produs.getPret();
        }

        return total;
    }

    public void genereazaPlata(int plataId, String metodaPlata)
    {
        this.plata = new Plata(plataId, calculeazaTotal(), metodaPlata);
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", client=" + client.getNume() +
                ", restaurant=" + restaurant.getNume() +
                ", produse=" + produse +
                ", sofer=" + (sofer != null ? sofer.getNume() : "neatribuit") +
                ", status=" + status +
                ", total=" + calculeazaTotal() +
                ", plata=" + plata +
                '}';
    }
}