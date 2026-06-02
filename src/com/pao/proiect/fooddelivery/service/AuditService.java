package com.pao.proiect.fooddelivery.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

// Clasa singleton pentru gestionarea auditului actiunilor utilizatorilor
// Scrie in audit.csv fiecare actiune importanta din aplicatie
// Fisierul este deschis in modul append pentru a adauga noi intrari fara a sterge cele vechi
public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";

    // Constructor privat pentru a preveni instantierea directa
    private AuditService() {
        initializeAuditFile();
    }

    // Metoda pentru a obtine instanta singleton
    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }

        return instance;
    }

    // Metoda pentru a initializa fisierul de audit daca nu exista sau este gol
    private void initializeAuditFile() {
        File file = new File(AUDIT_FILE);

        if (!file.exists() || file.length() == 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write("nume_actiune,timestamp");
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Eroare la initializarea fisierului de audit: " + e.getMessage());
            }
        }
    }

    // Metoda pentru a loga o actiune in fisierul de audit
    public synchronized void logAction(String actionName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.write(actionName + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in audit.csv: " + e.getMessage());
        }
    }
}