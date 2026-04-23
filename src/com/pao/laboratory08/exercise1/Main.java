package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String comanda = reader.readLine();

        if (comanda == null || comanda.isEmpty()) {
            return;
        }

        String[] parti = comanda.split(" ", 2);
        String tip = parti[0];

        if (tip.equals("PRINT")) {
            for (Student student : studenti) {
                System.out.println(student);
            }
            return;
        }

        if (parti.length < 2) {
            return;
        }

        String numeCautat = parti[1];

        for (Student student : studenti) {
            if (student.getNume().equals(numeCautat)) {
                if (tip.equals("SHALLOW")) {
                    Student clona = student.shallowClone();
                    clona.getAdresa().setOras("MODIFICAT");

                    System.out.println("Original: " + student);
                    System.out.println("Clona: " + clona);
                    return;
                }

                if (tip.equals("DEEP")) {
                    Student clona = student.deepClone();
                    clona.getAdresa().setOras("MODIFICAT");

                    System.out.println("Original: " + student);
                    System.out.println("Clona: " + clona);
                    return;
                }
            }
        }
    }

    private static List<Student> citesteStudenti() throws Exception {
        List<Student> studenti = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String linie;

        while ((linie = br.readLine()) != null) {
            String[] parts = linie.split(",");

            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            Student student = new Student(nume, varsta, adresa);

            studenti.add(student);
        }

        br.close();
        return studenti;
    }
}