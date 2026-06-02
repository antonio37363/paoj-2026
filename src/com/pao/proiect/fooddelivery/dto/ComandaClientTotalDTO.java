package com.pao.proiect.fooddelivery.dto;

// Clasa care retine informatii despre o comanda, inclusiv numele clientului, numele restaurantului, statusul comenzii si totalul acesteia
public class ComandaClientTotalDTO {
    private int comandaId;
    private String clientNume;
    private String restaurantNume;
    private String status;
    private double total;

    public ComandaClientTotalDTO(int comandaId, String clientNume, String restaurantNume, String status, double total) {
        this.comandaId = comandaId;
        this.clientNume = clientNume;
        this.restaurantNume = restaurantNume;
        this.status = status;
        this.total = total;
    }

    public int getComandaId() {
        return comandaId;
    }

    public String getClientNume() {
        return clientNume;
    }

    public String getRestaurantNume() {
        return restaurantNume;
    }

    public String getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Comanda #" + comandaId +
                " | Client: " + clientNume +
                " | Restaurant: " + restaurantNume +
                " | Status: " + status +
                " | Total: " + total + " lei";
    }
}