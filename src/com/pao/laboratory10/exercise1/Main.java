package com.pao.laboratory10.exercise1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "ENQUEUE": {
                    coada.addLast(citesteTranzactie(scanner));
                    break;
                }

                case "PUSH": {
                    coada.addFirst(citesteTranzactie(scanner));
                    break;
                }

                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Procesat: " + coada.removeFirst());
                    }
                    break;
                }

                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        System.out.println("Extras: " + coada.removeFirst());
                    }
                    break;
                }

                case "REMOVE_DEBIT": {
                    int count = 0;
                    Iterator<Tranzactie> iterator = coada.iterator();

                    while (iterator.hasNext()) {
                        Tranzactie tranzactie = iterator.next();

                        if (tranzactie.getTip() == TipTranzactie.DEBIT) {
                            iterator.remove();
                            count++;
                        }
                    }

                    System.out.println("Eliminat " + count + " tranzactii DEBIT.");
                    break;
                }

                case "REMOVE_BELOW": {
                    double threshold = scanner.nextDouble();
                    int count = 0;
                    Iterator<Tranzactie> iterator = coada.iterator();

                    while (iterator.hasNext()) {
                        Tranzactie tranzactie = iterator.next();

                        if (tranzactie.getSuma() < threshold) {
                            iterator.remove();
                            count++;
                        }
                    }

                    System.out.printf(Locale.US,
                            "Eliminat %d tranzactii sub %.2f RON.%n",
                            count, threshold);
                    break;
                }

                case "PRINT": {
                    for (Tranzactie tranzactie : coada) {
                        System.out.println(tranzactie);
                    }
                    break;
                }

                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
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
}