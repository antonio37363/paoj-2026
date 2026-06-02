package com.pao.proiect.fooddelivery.exception;

// Exceptie pentru cazul in care un produs nu este gasit in baza de date
public class ProdusNotFoundException extends Exception
{
    public ProdusNotFoundException(String message)
    {
        super(message);
    }
}