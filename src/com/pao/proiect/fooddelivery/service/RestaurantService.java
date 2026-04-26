package com.pao.proiect.fooddelivery.service;

import com.pao.proiect.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.proiect.fooddelivery.model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantService
{
    private static RestaurantService instance;

    private List<Restaurant> restaurante;
    private Map<Integer, Restaurant> restauranteById;

    private RestaurantService()
    {
        this.restaurante = new ArrayList<>();
        this.restauranteById = new HashMap<>();
    }

    public static RestaurantService getInstance()
    {
        if (instance == null) {
            instance = new RestaurantService();
        }

        return instance;
    }

    public void adaugaRestaurant(Restaurant restaurant)
    {
        if (restaurant != null)
        {
            restaurante.add(restaurant);
            restauranteById.put(restaurant.getId(), restaurant);
        }
    }

    public void stergeRestaurantDupaId(int id) throws RestaurantNotFoundException
    {
        Restaurant restaurant = cautaRestaurantDupaId(id);

        restaurante.remove(restaurant);
        restauranteById.remove(id);
    }

    public Restaurant cautaRestaurantDupaId(int id) throws RestaurantNotFoundException
    {
        Restaurant restaurant = restauranteById.get(id);

        if (restaurant == null) {
            throw new RestaurantNotFoundException("Restaurantul cu id-ul " + id + " nu a fost gasit.");
        }

        return restaurant;
    }

    public Restaurant cautaRestaurantDupaNume(String nume) throws RestaurantNotFoundException
    {
        for (Restaurant restaurant : restaurante)
        {
            if (restaurant.getNume().equalsIgnoreCase(nume))
            {
                return restaurant;
            }
        }

        throw new RestaurantNotFoundException("Restaurantul cu numele " + nume + " nu a fost gasit.");
    }

    public List<Restaurant> listeazaRestaurante() {
        return restaurante;
    }
}