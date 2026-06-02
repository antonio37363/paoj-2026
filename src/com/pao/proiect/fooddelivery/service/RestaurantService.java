package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.proiect.fooddelivery.model.Produs;
import com.pao.proiect.fooddelivery.model.Restaurant;
import com.pao.proiect.fooddelivery.repository.ProdusRepository;
import com.pao.proiect.fooddelivery.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Service pentru gestionarea restaurantelor si produselor acestora
public class RestaurantService {
    private static RestaurantService instance;

    private final RestaurantRepository restaurantRepository;
    private final ProdusRepository produsRepository;

    private final List<Restaurant> restaurante;
    private final Map<Integer, Restaurant> restauranteById;

    // Constructor privat pentru a preveni instantierea directa
    private RestaurantService() {
        this.restaurantRepository = new RestaurantRepository();
        this.produsRepository = new ProdusRepository();
        this.restaurante = new ArrayList<>();
        this.restauranteById = new HashMap<>();
    }

    // Metoda pentru a obtine instanta 
    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }

        return instance;
    }

    // Metoda pentru a adauga un restaurant nou
    public void adaugaRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            return;
        }

        restaurante.add(restaurant);
        restauranteById.put(restaurant.getId(), restaurant);

        restaurantRepository.save(restaurant);

        for (Produs produs : restaurant.getMeniu().getProduse()) {
            produsRepository.save(produs, restaurant.getId());
        }
    }

    // Metoda pentru a adauga un produs nou intr-un restaurant
    public void adaugaProdusInRestaurant(int restaurantId, Produs produs) throws RestaurantNotFoundException {
        if (produs == null) {
            return;
        }

        Restaurant restaurant = cautaRestaurantDupaId(restaurantId);
        restaurant.getMeniu().adaugaProdus(produs);

        produsRepository.save(produs, restaurantId);
    }

    // Metoda pentru a sterge un restaurant dupa id
    public void stergeRestaurantDupaId(int id) throws RestaurantNotFoundException {
        Restaurant restaurant = cautaRestaurantDupaId(id);

        restaurante.remove(restaurant);
        restauranteById.remove(id);

        restaurantRepository.delete(id);
    }

    //  Metoda pentru a cauta un restaurant dupa id
    public Restaurant cautaRestaurantDupaId(int id) throws RestaurantNotFoundException {
        Restaurant restaurant = restauranteById.get(id);

        if (restaurant != null) {
            return restaurant;
        }

        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);

        if (restaurantOptional.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurantul cu id-ul " + id + " nu a fost gasit.");
        }

        restaurant = restaurantOptional.get();
        incarcaProduseleRestaurantului(restaurant);

        restaurante.add(restaurant);
        restauranteById.put(restaurant.getId(), restaurant);

        return restaurant;
    }

    // Metoda pentru a cauta un restaurant dupa nume
    public Restaurant cautaRestaurantDupaNume(String nume) throws RestaurantNotFoundException {
        for (Restaurant restaurant : listeazaRestaurante()) {
            if (restaurant.getNume().equalsIgnoreCase(nume)) {
                return restaurant;
            }
        }

        throw new RestaurantNotFoundException("Restaurantul cu numele " + nume + " nu a fost gasit.");
    }

    // Metoda pentru a lista toate restaurantele
    public List<Restaurant> listeazaRestaurante() {
        List<Restaurant> restauranteDinDb = restaurantRepository.findAll();

        restaurante.clear();
        restauranteById.clear();

        for (Restaurant restaurant : restauranteDinDb) {
            incarcaProduseleRestaurantului(restaurant);
            restaurante.add(restaurant);
            restauranteById.put(restaurant.getId(), restaurant);
        }

        return restaurante;
    }

    // Metoda pentru a incarca produsele unui restaurant
    private void incarcaProduseleRestaurantului(Restaurant restaurant) {
        List<Produs> produse = produsRepository.findByRestaurantId(restaurant.getId());

        for (Produs produs : produse) {
            restaurant.getMeniu().adaugaProdus(produs);
        }
    }
}
