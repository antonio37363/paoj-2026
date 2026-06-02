package com.pao.proiect.fooddelivery.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Constructor privat pentru a preveni instantierea directa
    private DatabaseConnection() {
        connect();
    }

    // Luam datele de cinectare din fisierul db.properties si ne conectam la baza de date
    private void connect() {
        try {
            Properties properties = new Properties();

            try (InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("db.properties")) {

                if (input == null) {
                    throw new RuntimeException("Fisierul db.properties nu a fost gasit.");
                }

                properties.load(input);
            }

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);

        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea fisierului db.properties", e);
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea la baza de date", e);
        }
    }

    // Metoda pentru a obtine instanta unica a clasei 
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    // Metoda pentru a obtine conexiunea la baza de date
    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la verificarea conexiunii", e);
        }

        return connection;
    }

    // Metoda pentru a inchide conexiunea la baza de date
    public synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Eroare la inchiderea conexiunii: " + e.getMessage());
        }
    }
}
