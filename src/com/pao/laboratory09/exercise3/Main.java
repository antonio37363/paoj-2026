package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii(5);

        ProcessorThread processor = new ProcessorThread(coada);
        Thread processorThread = new Thread(processor);

        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        processorThread.start();

        atm1.start();
        atm2.start();
        atm3.start();

        atm1.join();
        atm2.join();
        atm3.join();

        processor.activ = false;
        coada.trezesteToateFirele();

        processorThread.join();

        System.out.println("Toate tranzactiile procesate. Total: " + processor.getTotalProcesate());
    }
}

class Tranzactie {
    int id;
    double suma;
    String data;

    public Tranzactie(int id, double suma, String data) {
        this.id = id;
        this.suma = suma;
        this.data = data;
    }
}

class CoadaTranzactii {
    private final Queue<Tranzactie> coada = new LinkedList<>();
    private final int capacitate;

    public CoadaTranzactii(int capacitate) {
        this.capacitate = capacitate;
    }

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (coada.size() == capacitate) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }

        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage(ProcessorThread processor) throws InterruptedException {
        while (coada.isEmpty()) {
            if (!processor.activ) {
                return null;
            }

            wait();
        }

        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized void trezesteToateFirele() {
        notifyAll();
    }
}

class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 4; i++) {
            int tranzactieId = atmId * 100 + i;
            double suma = atmId * 1000 + i * 100;
            String data = "2024-01-" + String.format("%02d", i);

            Tranzactie t = new Tranzactie(tranzactieId, suma, data);

            System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n",
                    atmId, t.id, t.suma);

            try {
                coada.adauga(t, atmId);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

class ProcessorThread implements Runnable {
    private final CoadaTranzactii coada;
    volatile boolean activ = true;
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (activ || totalProcesate < 12) {
            try {
                Tranzactie t = coada.extrage(this);

                if (t == null) {
                    break;
                }

                Thread.sleep(80);

                System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n",
                        t.id, t.suma, t.data);

                totalProcesate++;
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public int getTotalProcesate() {
        return totalProcesate;
    }
}