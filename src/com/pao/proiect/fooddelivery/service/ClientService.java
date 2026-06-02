package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.ClientNotFoundException;
import com.pao.proiect.fooddelivery.model.Client;
import com.pao.proiect.fooddelivery.repository.ClientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Service pentru gestionarea clientilor
public class ClientService {
    private static ClientService instance;

    private final ClientRepository clientRepository;

    private final List<Client> clienti;
    private final Map<Integer, Client> clientiById;

    // Constructor privat pentru a preveni instantierea directa
    private ClientService() {
        this.clientRepository = new ClientRepository();
        this.clienti = new ArrayList<>();
        this.clientiById = new HashMap<>();
    }

    // Metoda pentru a obtine instanta singleton a ClientService
    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }

        return instance;
    }

    // Metoda pentru a adauga un client nou
    public void adaugaClient(Client client) {
        if (client == null) {
            return;
        }

        clienti.add(client);
        clientiById.put(client.getId(), client);

        clientRepository.save(client);
    }

    // Metoda pentru a sterge un client dupa id
    public void stergeClientDupaId(int id) throws ClientNotFoundException {
        Client client = cautaClientDupaId(id);

        clienti.remove(client);
        clientiById.remove(id);

        clientRepository.delete(id);
    }

    //  Metoda pentru a cauta un client dupa id
    public Client cautaClientDupaId(int id) throws ClientNotFoundException {
        Client client = clientiById.get(id);

        if (client != null) {
            return client;
        }

        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException("Clientul cu id-ul " + id + " nu a fost gasit.");
        }

        client = clientOptional.get();
        clienti.add(client);
        clientiById.put(client.getId(), client);

        return client;
    }

    // Metoda pentru a cauta un client dupa nume
    public Client cautaClientDupaNume(String nume) throws ClientNotFoundException {
        for (Client client : listeazaClienti()) {
            if (client.getNume().equalsIgnoreCase(nume)) {
                return client;
            }
        }

        throw new ClientNotFoundException("Clientul cu numele " + nume + " nu a fost gasit.");
    }

    // Metoda pentru a lista toti clientii
    public List<Client> listeazaClienti() {
        List<Client> clientiDinDb = clientRepository.findAll();

        clienti.clear();
        clientiById.clear();

        for (Client client : clientiDinDb) {
            clienti.add(client);
            clientiById.put(client.getId(), client);
        }

        return clienti;
    }
}
