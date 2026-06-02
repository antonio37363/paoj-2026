package com.pao.proiect.fooddelivery.dto;

// Clasa care retine informatii despre un produs vandut, inclusiv numele produsului, categoria si totalul vandut
public class ProdusVandutDTO {
    private int produsId;
    private String produsNume;
    private String categorie;
    private int totalVandut;

    public ProdusVandutDTO(int produsId, String produsNume, String categorie, int totalVandut) {
        this.produsId = produsId;
        this.produsNume = produsNume;
        this.categorie = categorie;
        this.totalVandut = totalVandut;
    }

    public int getProdusId() {
        return produsId;
    }

    public String getProdusNume() {
        return produsNume;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getTotalVandut() {
        return totalVandut;
    }

    @Override
    public String toString() {
        return produsNume +
                " | Categorie: " + categorie +
                " | Vandut: " + totalVandut + " bucati";
    }
}