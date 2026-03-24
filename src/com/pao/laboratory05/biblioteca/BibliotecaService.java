package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti;

    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] temp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, temp, 0, carti.length);
        temp[temp.length - 1] = carte;
        carti = temp;

        System.out.println("Carte adăugată: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy);
        afiseazaCarti(copy);
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);
        afiseazaCarti(copy);
    }

    private void afiseazaCarti(Carte[] cartiDeAfisat) {
        for (int i = 0; i < cartiDeAfisat.length; i++) {
            System.out.println((i + 1) + ". " + cartiDeAfisat[i]);
        }
    }
}