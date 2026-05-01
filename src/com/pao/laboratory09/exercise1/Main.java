package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) {
            return;
        }

        int n = sc.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            double suma = Double.parseDouble(sc.next());
            String data = sc.next();
            String contSursa = sc.next();
            String contDestinatie = sc.next();
            TipTranzactie tip = TipTranzactie.valueOf(sc.next());

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.note = "procesat";

            tranzactii.add(t);
        }

        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(tranzactii);
        }

        List<Tranzactie> tranzactiiDeserializate;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            tranzactiiDeserializate = (List<Tranzactie>) in.readObject();
        }

        while (sc.hasNext()) {
            String comanda = sc.next();

            if (comanda.equals("LIST")) {
                for (Tranzactie t : tranzactiiDeserializate) {
                    System.out.println(t);
                }
            } else if (comanda.equals("FILTER")) {
                String prefix = sc.next();
                boolean gasit = false;

                for (Tranzactie t : tranzactiiDeserializate) {
                    if (t.data.startsWith(prefix)) {
                        System.out.println(t);
                        gasit = true;
                    }
                }

                if (!gasit) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (comanda.equals("NOTE")) {
                int id = sc.nextInt();
                Tranzactie gasita = null;

                for (Tranzactie t : tranzactiiDeserializate) {
                    if (t.id == id) {
                        gasita = t;
                        break;
                    }
                }

                if (gasita == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + gasita.note);
                }
            }
        }
    }
}

class Tranzactie implements Serializable {
    private static final long serialVersionUID = 1L;

    int id;
    double suma;
    String data;
    String contSursa;
    String contDestinatie;
    TipTranzactie tip;
    transient String note;

    public Tranzactie(int id, double suma, String data, String contSursa,
                      String contDestinatie, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.contSursa = contSursa;
        this.contDestinatie = contDestinatie;
        this.tip = tip;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "[%d] %s %s: %.2f RON | %s -> %s",
                id, data, tip, suma, contSursa, contDestinatie);
    }
}