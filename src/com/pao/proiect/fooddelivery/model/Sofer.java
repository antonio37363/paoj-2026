package com.pao.proiect.fooddelivery.model;

public class Sofer extends Angajat
{
    private String numarMasina;
    private boolean disponibil;

    public Sofer(int id, String nume, String telefon, double salariu, String numarMasina)
    {
        super(id, nume, telefon, salariu);
        this.numarMasina = numarMasina;
        this.disponibil = true;
    }

    public String getNumarMasina() {
        return numarMasina;
    }

    public void setNumarMasina(String numarMasina) {
        this.numarMasina = numarMasina;
    }

    public boolean isDisponibil() {
        return disponibil;
    }

    public void setDisponibil(boolean disponibil) {
        this.disponibil = disponibil;
    }

    @Override
    public String getRol() {
        return "Sofer";
    }

    @Override
    public String toString() {
        return "Sofer{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", salariu=" + salariu +
                ", numarMasina='" + numarMasina + '\'' +
                ", disponibil=" + disponibil +
                '}';
    }
}