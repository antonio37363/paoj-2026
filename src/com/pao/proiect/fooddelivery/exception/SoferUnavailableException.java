package com.pao.proiect.fooddelivery.exception;

// Exceptie pentru cazul in care nu exista soferi disponibili pentru a prelua o comanda
public class SoferUnavailableException extends Exception
{
    public SoferUnavailableException(String message)
    {
        super(message);
    }
}