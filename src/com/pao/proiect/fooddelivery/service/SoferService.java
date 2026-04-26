package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.SoferUnavailableException;
import com.pao.proiect.fooddelivery.model.Sofer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoferService
{
    private static SoferService instance;

    private List<Sofer> soferi;
    private Map<Integer, Sofer> soferiById;

    private SoferService()
    {
        this.soferi = new ArrayList<>();
        this.soferiById = new HashMap<>();
    }

    public static SoferService getInstance()
    {
        if (instance == null)
        {
            instance = new SoferService();
        }

        return instance;
    }

    public void adaugaSofer(Sofer sofer)
    {
        if (sofer != null)
        {
            soferi.add(sofer);
            soferiById.put(sofer.getId(), sofer);
        }
    }

    public void stergeSoferDupaId(int id) throws SoferUnavailableException
    {
        Sofer sofer = cautaSoferDupaId(id);

        soferi.remove(sofer);
        soferiById.remove(id);
    }

    public Sofer cautaSoferDupaId(int id) throws SoferUnavailableException
    {
        Sofer sofer = soferiById.get(id);

        if (sofer == null)
        {
            throw new SoferUnavailableException("Soferul cu id-ul " + id + " nu a fost gasit.");
        }

        return sofer;
    }

    public Sofer cautaSoferDisponibil() throws SoferUnavailableException
    {
        for (Sofer sofer : soferi)
        {
            if (sofer.isDisponibil())
            {
                return sofer;
            }
        }

        throw new SoferUnavailableException("Nu exista niciun sofer disponibil.");
    }

    public List<Sofer> listeazaSoferi() {
        return soferi;
    }

    public List<Sofer> listeazaSoferiDisponibili()
    {
        List<Sofer> disponibili = new ArrayList<>();

        for (Sofer sofer : soferi)
        {
            if (sofer.isDisponibil())
            {
                disponibili.add(sofer);
            }
        }

        return disponibili;
    }
}