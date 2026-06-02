package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.SoferUnavailableException;
import com.pao.proiect.fooddelivery.model.Sofer;
import com.pao.proiect.fooddelivery.repository.SoferRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Service pentru gestionarea soferilor
public class SoferService {
    private static SoferService instance;

    private final SoferRepository soferRepository;
    private List<Sofer> soferi;
    private Map<Integer, Sofer> soferiById;

    // Constructor privat pentru a preveni instantierea directa
    private SoferService() {
        this.soferRepository = new SoferRepository();
        this.soferi = new ArrayList<>();
        this.soferiById = new HashMap<>();
    }

    // Metoda pentru a obtine instanta singleton a serviciului
    public static SoferService getInstance() {
        if (instance == null) {
            instance = new SoferService();
        }

        return instance;
    }

    // Metoda pentru a adauga un sofer nou
    public void adaugaSofer(Sofer sofer) {
        if (sofer != null) {
            soferi.add(sofer);
            soferiById.put(sofer.getId(), sofer);
            soferRepository.save(sofer);
        }
    }

    // Metoda pentru a sterge un sofer dupa id
    public void stergeSoferDupaId(int id) throws SoferUnavailableException {
        Sofer sofer = cautaSoferDupaId(id);

        soferi.remove(sofer);
        soferiById.remove(id);
        soferRepository.delete(id);
    }

    // Metoda pentru a cauta un sofer dupa id
    public Sofer cautaSoferDupaId(int id) throws SoferUnavailableException {
        Sofer sofer = soferiById.get(id);

        if (sofer != null) {
            return sofer;
        }

        Optional<Sofer> soferOptional = soferRepository.findById(id);

        if (soferOptional.isPresent()) {
            sofer = soferOptional.get();
            soferi.add(sofer);
            soferiById.put(sofer.getId(), sofer);
            return sofer;
        }

        throw new SoferUnavailableException("Soferul cu id-ul " + id + " nu a fost gasit.");
    }

    // Metoda pentru a cauta un sofer disponibil
    public Sofer cautaSoferDisponibil() throws SoferUnavailableException {
        for (Sofer sofer : soferi) {
            if (sofer.isDisponibil()) {
                return sofer;
            }
        }

        Optional<Sofer> soferOptional = soferRepository.findFirstAvailable();

        if (soferOptional.isPresent()) {
            Sofer sofer = soferOptional.get();
            soferi.add(sofer);
            soferiById.put(sofer.getId(), sofer);
            return sofer;
        }

        throw new SoferUnavailableException("Nu exista niciun sofer disponibil.");
    }

    // Metoda pentru a lista toti soferii
    public List<Sofer> listeazaSoferi() {
        List<Sofer> soferiDinDb = soferRepository.findAll();
        sincronizeazaCache(soferiDinDb);
        return soferiDinDb;
    }

    // Metoda pentru a lista doar soferii disponibili
    public List<Sofer> listeazaSoferiDisponibili() {
        List<Sofer> soferiDisponibili = soferRepository.findAllAvailable();

        for (Sofer sofer : soferiDisponibili) {
            soferiById.put(sofer.getId(), sofer);
        }

        return soferiDisponibili;
    }

    // Metoda pentru a actualiza disponibilitatea unui sofer
    public void actualizeazaDisponibilitate(int soferId, boolean disponibil) throws SoferUnavailableException {
        Sofer sofer = cautaSoferDupaId(soferId);
        sofer.setDisponibil(disponibil);
        soferRepository.updateDisponibilitate(soferId, disponibil);
    }

    // Metoda pentru a sincroniza lista de soferi din baza de date cu lista din memorie
    private void sincronizeazaCache(List<Sofer> soferiDinDb) {
        this.soferi = new ArrayList<>(soferiDinDb);
        this.soferiById = new HashMap<>();

        for (Sofer sofer : soferiDinDb) {
            this.soferiById.put(sofer.getId(), sofer);
        }
    }
}
