package com.pao.proiect.fooddelivery.exception;

// Exceptie pentru cazul in care restaurantul cautat nu este gasit in baza de date
public class RestaurantNotFoundException extends Exception
{
    public RestaurantNotFoundException(String message)
    {
        super(message);
    }
}