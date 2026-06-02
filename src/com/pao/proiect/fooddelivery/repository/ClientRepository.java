package com.pao.proiect.fooddelivery.repository;

import com.pao.proiect.fooddelivery.model.Adresa;
import com.pao.proiect.fooddelivery.model.Client;
import com.pao.proiect.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Repository pentru entitatea Client, care se ocupa de operatiile CRUD asupra clientilor din baza de date
public class ClientRepository implements Repository<Client, Integer> {

    // Implementarea metodei save, care insereaza un client nou in baza de date, impreuna cu adresa acestuia
    @Override
    public void save(Client client) {
        String insertAdresaSql = """
                INSERT INTO adrese(oras, strada, numar)
                VALUES (?, ?, ?)
                """;

        String insertClientSql = """
                INSERT INTO clienti(id, nume, telefon, adresa_id)
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            int adresaId;

            // Inseram adresa clientului si obtinem id-ul generat pentru aceasta
            try (PreparedStatement adresaStmt = connection.prepareStatement(
                    insertAdresaSql,
                    new String[]{"ID"}
            )) {
                adresaStmt.setString(1, client.getAdresa().getOras());
                adresaStmt.setString(2, client.getAdresa().getStrada());
                adresaStmt.setString(3, client.getAdresa().getNumar());

                adresaStmt.executeUpdate();

                try (ResultSet rs = adresaStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        adresaId = rs.getInt(1);
                    } else {
                        throw new SQLException("Nu s-a putut obtine id-ul adresei.");
                    }
                }
            }

            // Inseram clientul, folosind id-ul adresei inserate anterior
            try (PreparedStatement clientStmt = connection.prepareStatement(insertClientSql)) {
                clientStmt.setInt(1, client.getId());
                clientStmt.setString(2, client.getNume());
                clientStmt.setString(3, client.getTelefon());
                clientStmt.setInt(4, adresaId);

                clientStmt.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println("Eroare la rollback: " + rollbackException.getMessage());
            }

            System.out.println("Eroare la salvarea clientului: " + e.getMessage());

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Eroare la resetarea autoCommit: " + e.getMessage());
            }
        }
    }

    // Implementarea metodei findById, care cauta un client dupa id-ul acestuia in baza de date 
    @Override
    public Optional<Client> findById(Integer id) {
        String sql = """
                SELECT 
                    c.id AS client_id,
                    c.nume AS client_nume,
                    c.telefon AS client_telefon,
                    a.oras AS adresa_oras,
                    a.strada AS adresa_strada,
                    a.numar AS adresa_numar
                FROM clienti c
                JOIN adrese a ON c.adresa_id = a.id
                WHERE c.id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);

            // Executam interogarea si construim obiectul Client din rezultatele obtinute
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Adresa adresa = new Adresa(
                            rs.getString("adresa_oras"),
                            rs.getString("adresa_strada"),
                            rs.getString("adresa_numar")
                    );

                    Client client = new Client(
                            rs.getInt("client_id"),
                            rs.getString("client_nume"),
                            rs.getString("client_telefon"),
                            adresa
                    );

                    return Optional.of(client);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la cautarea clientului: " + e.getMessage());
        }

        return Optional.empty();
    }

    // Implementarea metodei findAll, care returneaza o lista cu toti clientii din baza de date, impreuna cu adresele acestora
    @Override
    public List<Client> findAll() {
        String sql = """
                SELECT 
                    c.id AS client_id,
                    c.nume AS client_nume,
                    c.telefon AS client_telefon,
                    a.oras AS adresa_oras,
                    a.strada AS adresa_strada,
                    a.numar AS adresa_numar
                FROM clienti c
                JOIN adrese a ON c.adresa_id = a.id
                """;

        List<Client> clienti = new ArrayList<>();

        // Executam interogarea si construim lista de clienti din rezultatele obtinute
        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Adresa adresa = new Adresa(
                        rs.getString("adresa_oras"),
                        rs.getString("adresa_strada"),
                        rs.getString("adresa_numar")
                );

                Client client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("client_nume"),
                        rs.getString("client_telefon"),
                        adresa
                );

                clienti.add(client);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea clientilor: " + e.getMessage());
        }

        return clienti;
    }

    // Implementarea metodei update, care actualizeaza datele unui client existent in baza de date, impreuna cu adresa acestuia
    @Override
    public void update(Client client) {
        String sql = """
                UPDATE clienti
                SET nume = ?, telefon = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, client.getNume());
            stmt.setString(2, client.getTelefon());
            stmt.setInt(3, client.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea clientului: " + e.getMessage());
        }
    }

    // Implementarea metodei delete, care sterge un client din baza de date dupa id-ul acestuia
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM clienti WHERE id = ?";

        // Stergem clientul din baza de date folosind id-ul acestuia
        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea clientului: " + e.getMessage());
        }
    }
}
