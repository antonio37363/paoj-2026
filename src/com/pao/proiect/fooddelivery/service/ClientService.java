package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.ClientNotFoundException;
import com.pao.proiect.fooddelivery.model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService
{
    private static ClientService instance;

    private List<Client> clienti;
    private Map<Integer, Client> clientiById;

    private ClientService()
    {
        this.clienti = new ArrayList<>();
        this.clientiById = new HashMap<>();
    }

    public static ClientService getInstance()
    {
        if (instance == null)
        {
            instance = new ClientService();
        }

        return instance;
    }

    public void adaugaClient(Client client)
    {
        if (client != null)
        {
            clienti.add(client);
            clientiById.put(client.getId(), client);
        }
    }

    public void stergeClientDupaId(int id) throws ClientNotFoundException
    {
        Client client = cautaClientDupaId(id);

        clienti.remove(client);
        clientiById.remove(id);
    }

    public Client cautaClientDupaId(int id) throws ClientNotFoundException
    {
        Client client = clientiById.get(id);

        if (client == null)
        {
            throw new ClientNotFoundException("Clientul cu id-ul " + id + " nu a fost gasit.");
        }

        return client;
    }

    public Client cautaClientDupaNume(String nume) throws ClientNotFoundException
    {
        for (Client client : clienti)
        {
            if (client.getNume().equalsIgnoreCase(nume)) {
                return client;
            }
        }

        throw new ClientNotFoundException("Clientul cu numele " + nume + " nu a fost gasit.");
    }

    public List<Client> listeazaClienti() {
        return clienti;
    }
}