package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.SoferUnavailableException;
import com.pao.proiect.fooddelivery.model.Client;
import com.pao.proiect.fooddelivery.model.Comanda;
import com.pao.proiect.fooddelivery.model.Restaurant;
import com.pao.proiect.fooddelivery.model.Sofer;
import com.pao.proiect.fooddelivery.model.StatusComanda;
import com.pao.proiect.fooddelivery.repository.ComandaRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Service pentru gestionarea comenzilor
public class ComandaService {
    private static ComandaService instance;

    private final List<Comanda> comenzi;
    private final Map<Integer, Comanda> comenziById;
    private final ComandaRepository comandaRepository;

    // Constructor privat pentru a preveni instantierea directa
    private ComandaService() {
        this.comenzi = new ArrayList<>();
        this.comenziById = new HashMap<>();
        this.comandaRepository = new ComandaRepository();
    }

    // Metoda pentru a obtine instanta 
    public static ComandaService getInstance() {
        if (instance == null) {
            instance = new ComandaService();
        }

        return instance;
    }

    // Metoda pentru a plasa o comanda noua
    public Comanda plaseazaComanda(int id, Client client, Restaurant restaurant) {
        Comanda comanda = new Comanda(id, client, restaurant);

        comenzi.add(comanda);
        comenziById.put(id, comanda);
        client.adaugaComanda(comanda);

        return comanda;
    }

    // Metoda pentru a salva o comanda in baza de date
    public void salveazaComanda(Comanda comanda) {
        if (comanda == null) {
            return;
        }

        comandaRepository.save(comanda);
    }

    // Metoda pentru a cauta o comanda dupa id
    public Comanda cautaComandaDupaId(int id) {
        Comanda comanda = comenziById.get(id);

        if (comanda != null) {
            return comanda;
        }

        Optional<Comanda> comandaOptional = comandaRepository.findById(id);

        if (comandaOptional.isPresent()) {
            comanda = comandaOptional.get();
            comenzi.add(comanda);
            comenziById.put(comanda.getId(), comanda);
            return comanda;
        }

        return null;
    }

    // Metoda pentru a sterge o comanda dupa id
    public void stergeComandaDupaId(int id) {
        Comanda comanda = comenziById.get(id);

        if (comanda != null) {
            comenzi.remove(comanda);
            comenziById.remove(id);
        }

        comandaRepository.delete(id);
    }

    // Metoda pentru a lista toate comenzile
    public List<Comanda> listeazaComenzi() {
        List<Comanda> comenziDinBazaDeDate = comandaRepository.findAll();

        comenzi.clear();
        comenziById.clear();

        for (Comanda comanda : comenziDinBazaDeDate) {
            comenzi.add(comanda);
            comenziById.put(comanda.getId(), comanda);
        }

        return comenzi;
    }

    // Metoda pentru a lista comenzile unui client
    public List<Comanda> listeazaComenziClient(Client client) {
        if (client == null) {
            return new ArrayList<>();
        }

        List<Comanda> comenziClient = comandaRepository.findByClientId(client.getId());

        for (Comanda comanda : comenziClient) {
            comenziById.put(comanda.getId(), comanda);

            if (!comenzi.contains(comanda)) {
                comenzi.add(comanda);
            }
        }

        return comenziClient;
    }

    // Metoda pentru a atribui un sofer unei comenzi
    public void atribuieSofer(Comanda comanda, Sofer sofer) throws SoferUnavailableException {
        if (sofer == null || !sofer.isDisponibil()) {
            throw new SoferUnavailableException("Soferul nu este disponibil.");
        }

        comanda.atribuieSofer(sofer);
        sofer.setDisponibil(false);
        comanda.schimbaStatus(StatusComanda.IN_LIVRARE);
    }

    // Metoda pentru a schimba statusul unei comenzi
    public void schimbaStatusComanda(Comanda comanda, StatusComanda status) {
        if (comanda != null && status != null) {
            comanda.schimbaStatus(status);
            comandaRepository.update(comanda);
        }
    }

    // Metoda pentru a lista comenzile sortate dupa total
    public List<Comanda> listeazaComenziSortateDupaTotal() {
        List<Comanda> copie = new ArrayList<>(listeazaComenzi());

        copie.sort(Comparator.comparingDouble(Comanda::calculeazaTotal));

        return copie;
    }
}
