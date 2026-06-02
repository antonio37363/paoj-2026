package com.pao.proiect.fooddelivery.exception;

// Exceptie pentru  clientul care nu a fost gasit in baza de date
public class ClientNotFoundException extends Exception
{
    public ClientNotFoundException(String message)
    {
        super(message);
    }
}