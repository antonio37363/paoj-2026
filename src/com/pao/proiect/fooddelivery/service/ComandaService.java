package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.SoferUnavailableException;
import com.pao.proiect.fooddelivery.model.Client;
import com.pao.proiect.fooddelivery.model.Comanda;
import com.pao.proiect.fooddelivery.model.Restaurant;
import com.pao.proiect.fooddelivery.model.Sofer;
import com.pao.proiect.fooddelivery.model.StatusComanda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComandaService
{
    private static ComandaService instance;

    private List<Comanda> comenzi;
    private Map<Integer, Comanda> comenziById;

    private ComandaService()
    {
        this.comenzi = new ArrayList<>();
        this.comenziById = new HashMap<>();
    }

    public static ComandaService getInstance()
    {
        if (instance == null)
        {
            instance = new ComandaService();
        }

        return instance;
    }

    public Comanda plaseazaComanda(int id, Client client, Restaurant restaurant)
    {
        Comanda comanda = new Comanda(id, client, restaurant);

        comenzi.add(comanda);
        comenziById.put(id, comanda);

        client.adaugaComanda(comanda);

        return comanda;
    }

    public Comanda cautaComandaDupaId(int id)
    {
        return comenziById.get(id);
    }

    public void stergeComandaDupaId(int id)
    {
        Comanda comanda = comenziById.get(id);

        if (comanda != null)
        {
            comenzi.remove(comanda);
            comenziById.remove(id);
        }
    }

    public List<Comanda> listeazaComenzi() {
        return comenzi;
    }

    public List<Comanda> listeazaComenziClient(Client client)
    {
        List<Comanda> rezultat = new ArrayList<>();

        for (Comanda comanda : comenzi)
        {
            if (comanda.getClient().equals(client))
            {
                rezultat.add(comanda);
            }
        }

        return rezultat;
    }

    public void atribuieSofer(Comanda comanda, Sofer sofer) throws SoferUnavailableException
    {
        if (sofer == null || !sofer.isDisponibil())
        {
            throw new SoferUnavailableException("Soferul nu este disponibil.");
        }

        comanda.atribuieSofer(sofer);
        sofer.setDisponibil(false);
        comanda.schimbaStatus(StatusComanda.IN_LIVRARE);
    }

    public void schimbaStatusComanda(Comanda comanda, StatusComanda status)
    {
        if (comanda != null && status != null)
        {
            comanda.schimbaStatus(status);
        }
    }

    public List<Comanda> listeazaComenziSortateDupaTotal()
    {
        List<Comanda> copie = new ArrayList<>(comenzi);

        copie.sort(Comparator.comparingDouble(Comanda::calculeazaTotal));

        return copie;
    }
}