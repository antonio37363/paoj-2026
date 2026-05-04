package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            tranzactii.add(citesteTranzactie(scanner));
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "UNIQUE_IDS": {
                    Set<Integer> idsUnice = new LinkedHashSet<>();

                    for (Tranzactie tranzactie : tranzactii) {
                        idsUnice.add(tranzactie.getId());
                    }

                    System.out.println("IDs unice (" + idsUnice.size() + "): " + idsUnice);
                    break;
                }

                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> raport = new TreeMap<>();

                    for (Tranzactie tranzactie : tranzactii) {
                        String luna = tranzactie.getData().substring(0, 7);
                        raport.putIfAbsent(luna, new double[2]);

                        if (tranzactie.getTip() == TipTranzactie.CREDIT) {
                            raport.get(luna)[0] += tranzactie.getSuma();
                        } else {
                            raport.get(luna)[1] += tranzactie.getSuma();
                        }
                    }

                    for (String luna : raport.keySet()) {
                        double credit = raport.get(luna)[0];
                        double debit = raport.get(luna)[1];

                        System.out.printf(Locale.US,
                                "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                luna, credit, debit);
                    }
                    break;
                }

                case "TOP": {
                    int topN = scanner.nextInt();

                    List<Tranzactie> copie = new ArrayList<>(tranzactii);
                    copie.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                    System.out.println("Top " + topN + ":");

                    for (int i = 0; i < topN && i < copie.size(); i++) {
                        System.out.println(copie.get(i));
                    }
                    break;
                }

                case "SORT_ASC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    afiseazaLista(tranzactii);
                    break;
                }

                case "SORT_DESC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    afiseazaLista(tranzactii);
                    break;
                }

                case "REVERSE": {
                    Collections.reverse(tranzactii);
                    afiseazaLista(tranzactii);
                    break;
                }

                case "MIN_MAX": {
                    Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));

                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                }

                case "CME_DEMO": {
                    try {
                        for (Tranzactie tranzactie : tranzactii) {
                            tranzactii.remove(tranzactie);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }

                default:
                    break;
            }
        }
    }

    private static Tranzactie citesteTranzactie(Scanner scanner) {
        int id = scanner.nextInt();
        double suma = scanner.nextDouble();
        String data = scanner.next();
        TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

        return new Tranzactie(id, suma, data, tip);
    }

    private static void afiseazaLista(List<Tranzactie> tranzactii) {
        for (Tranzactie tranzactie : tranzactii) {
            System.out.println(tranzactie);
        }
    }
}