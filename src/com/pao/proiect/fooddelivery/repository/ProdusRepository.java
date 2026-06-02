package com.pao.proiect.fooddelivery.repository;

import com.pao.proiect.fooddelivery.dto.ProdusMeniuDTO;
import com.pao.proiect.fooddelivery.dto.ProdusVandutDTO;
import com.pao.proiect.fooddelivery.model.Produs;
import com.pao.proiect.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdusRepository implements Repository<Produs, Integer> {

    // Deoarece un produs trebuie sa apartina unui restaurant, salvarea lui necesita si id-ul restaurantului.
    @Override
    public void save(Produs produs) {
        throw new UnsupportedOperationException(
                "Pentru salvarea unui produs foloseste save(produs, restaurantId)."
        );
    }

    // Metoda custom pentru a salva un produs asociat unui restaurant.
    public void save(Produs produs, int restaurantId) {
        String sql = """
                INSERT INTO produse(id, nume, pret, categorie, restaurant_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, produs.getId());
            stmt.setString(2, produs.getNume());
            stmt.setDouble(3, produs.getPret());
            stmt.setString(4, produs.getCategorie());
            stmt.setInt(5, restaurantId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la salvarea produsului: " + e.getMessage());
        }
    }

    // Metoda pentru a salva un produs 
    @Override
    public Optional<Produs> findById(Integer id) {
        String sql = """
                SELECT id, nume, pret, categorie
                FROM produse
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produs produs = new Produs(
                            rs.getInt("id"),
                            rs.getString("nume"),
                            rs.getDouble("pret"),
                            rs.getString("categorie")
                    );

                    return Optional.of(produs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la cautarea produsului: " + e.getMessage());
        }

        return Optional.empty();
    }

    // Metoda pentru a citi toate produsele din baza de date.
    @Override
    public List<Produs> findAll() {
        String sql = """
                SELECT id, nume, pret, categorie
                FROM produse
                """;

        List<Produs> produse = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produs produs = new Produs(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getDouble("pret"),
                        rs.getString("categorie")
                );

                produse.add(produs);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea produselor: " + e.getMessage());
        }

        return produse;
    }

    // Metoda pentru a citi toate produsele asociate unui restaurant.
    public List<Produs> findByRestaurantId(int restaurantId) {
        String sql = """
                SELECT id, nume, pret, categorie
                FROM produse
                WHERE restaurant_id = ?
                """;

        List<Produs> produse = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produs produs = new Produs(
                            rs.getInt("id"),
                            rs.getString("nume"),
                            rs.getDouble("pret"),
                            rs.getString("categorie")
                    );

                    produse.add(produs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea produselor restaurantului: " + e.getMessage());
        }

        return produse;
    }

    // Metoda pentru a actualiza un produs.
    @Override
    public void update(Produs produs) {
        String sql = """
                UPDATE produse
                SET nume = ?, pret = ?, categorie = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, produs.getNume());
            stmt.setDouble(2, produs.getPret());
            stmt.setString(3, produs.getCategorie());
            stmt.setInt(4, produs.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea produsului: " + e.getMessage());
        }
    }

    // 
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM produse WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea produsului: " + e.getMessage());
        }
    }

    // Metoda pentru a citi toate produsele din meniul unui restaurant, alaturi de numele restaurantului.
    public List<ProdusMeniuDTO> findProduseDinMeniulRestaurantului(int restaurantId) {
        // Meniul unui restaurant
        String sql = """
                SELECT
                    p.id AS produs_id,
                    p.nume AS produs_nume,
                    p.pret AS pret,
                    p.categorie AS categorie,
                    r.nume AS restaurant_nume
                FROM produse p
                JOIN restaurante r ON p.restaurant_id = r.id
                WHERE r.id = ?
                """;

        List<ProdusMeniuDTO> rezultate = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProdusMeniuDTO dto = new ProdusMeniuDTO(
                            rs.getInt("produs_id"),
                            rs.getString("produs_nume"),
                            rs.getDouble("pret"),
                            rs.getString("categorie"),
                            rs.getString("restaurant_nume")
                    );

                    rezultate.add(dto);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea meniului restaurantului: " + e.getMessage());
        }

        return rezultate;
    }

    // Metoda pentru a citi top 5 produse vandute, alaturi de numele si categoria produsului.
    public List<ProdusVandutDTO> findTop5ProduseVandute() {
        // Top 5 produse vandute
        String sql = """
                SELECT
                    p.id AS produs_id,
                    p.nume AS produs_nume,
                    p.categorie AS categorie,
                    SUM(cp.cantitate) AS total_vandut
                FROM comanda_produse cp
                JOIN produse p ON cp.produs_id = p.id
                GROUP BY p.id, p.nume, p.categorie
                ORDER BY total_vandut DESC
                FETCH FIRST 5 ROWS ONLY
                """;

        List<ProdusVandutDTO> rezultate = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProdusVandutDTO dto = new ProdusVandutDTO(
                        rs.getInt("produs_id"),
                        rs.getString("produs_nume"),
                        rs.getString("categorie"),
                        rs.getInt("total_vandut")
                );

                rezultate.add(dto);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea topului de produse vandute: " + e.getMessage());
        }

        return rezultate;
    }
}
