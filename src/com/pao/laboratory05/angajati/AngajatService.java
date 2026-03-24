package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        this.angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] temp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, temp, 0, angajati.length);
        temp[temp.length - 1] = a;
        angajati = temp;

        System.out.println("Angajat adăugat: " + a.getNume());
    }

    public void printAll() {
        for (int i = 0; i < angajati.length; i++) {
            System.out.println((i + 1) + ". " + angajati[i]);
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;

        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(angajat);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}