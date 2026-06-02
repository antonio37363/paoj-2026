package com.pao.proiect.fooddelivery.dto;

// Clasa care retine informatii despre un produs din meniu, inclusiv numele produsului, pretul, categoria si numele restaurantului
public class ProdusMeniuDTO {
    private int produsId;
    private String produsNume;
    private double pret;
    private String categorie;
    private String restaurantNume;

    public ProdusMeniuDTO(int produsId, String produsNume, double pret, String categorie, String restaurantNume) {
        this.produsId = produsId;
        this.produsNume = produsNume;
        this.pret = pret;
        this.categorie = categorie;
        this.restaurantNume = restaurantNume;
    }

    public int getProdusId() {
        return produsId;
    }

    public String getProdusNume() {
        return produsNume;
    }

    public double getPret() {
        return pret;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getRestaurantNume() {
        return restaurantNume;
    }

    @Override
    public String toString() {
        return produsId + ". " + produsNume +
                " | Categorie: " + categorie +
                " | Pret: " + pret + " lei" +
                " | Restaurant: " + restaurantNume;
    }
}