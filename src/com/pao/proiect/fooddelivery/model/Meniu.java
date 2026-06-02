package com.pao.proiect.fooddelivery.model;

import java.util.TreeSet;

// Clasa Meniu reprezintă un meniu de produse disponibile pentru comanda în cadrul aplicației de food delivery.
public class Meniu
{
    private TreeSet<Produs> produse;

    public Meniu() {
        this.produse = new TreeSet<>();
    }

    public void adaugaProdus(Produs produs)
    {
        if (produs != null)
        {
            produse.add(produs);
        }
    }

    public void stergeProdus(Produs produs) {
        produse.remove(produs);
    }

    public Produs cautaProdusDupaNume(String nume) {
        for (Produs produs : produse)
        {
            if (produs.getNume().equalsIgnoreCase(nume))
            {
                return produs;
            }
        }

        return null;
    }

    public TreeSet<Produs> getProduse() {
        return produse;
    }

    @Override
    public String toString() {
        return "Meniu{" +
                "produse=" + produse +
                '}';
    }
}