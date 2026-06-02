package com.pao.proiect.fooddelivery.repository;

import com.pao.proiect.fooddelivery.model.Adresa;
import com.pao.proiect.fooddelivery.model.Restaurant;
import com.pao.proiect.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantRepository implements Repository<Restaurant, Integer> {

    @Override
    public void save(Restaurant restaurant) {
        String insertAdresaSql = """
                INSERT INTO adrese(oras, strada, numar)
                VALUES (?, ?, ?)
                """;

        String insertRestaurantSql = """
                INSERT INTO restaurante(id, nume, adresa_id)
                VALUES (?, ?, ?)
                """;

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            int adresaId;

            try (PreparedStatement adresaStmt = connection.prepareStatement(
                    insertAdresaSql,
                    new String[]{"ID"}
            )) {
                adresaStmt.setString(1, restaurant.getAdresa().getOras());
                adresaStmt.setString(2, restaurant.getAdresa().getStrada());
                adresaStmt.setString(3, restaurant.getAdresa().getNumar());

                adresaStmt.executeUpdate();

                try (ResultSet rs = adresaStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        adresaId = rs.getInt(1);
                    } else {
                        throw new SQLException("Nu s-a putut obtine id-ul adresei restaurantului.");
                    }
                }
            }

            try (PreparedStatement restaurantStmt = connection.prepareStatement(insertRestaurantSql)) {
                restaurantStmt.setInt(1, restaurant.getId());
                restaurantStmt.setString(2, restaurant.getNume());
                restaurantStmt.setInt(3, adresaId);

                restaurantStmt.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println("Eroare la rollback: " + rollbackException.getMessage());
            }

            System.out.println("Eroare la salvarea restaurantului: " + e.getMessage());

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Eroare la resetarea autoCommit: " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<Restaurant> findById(Integer id) {
        String sql = """
                SELECT
                    r.id AS restaurant_id,
                    r.nume AS restaurant_nume,
                    a.oras AS adresa_oras,
                    a.strada AS adresa_strada,
                    a.numar AS adresa_numar
                FROM restaurante r
                JOIN adrese a ON r.adresa_id = a.id
                WHERE r.id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Adresa adresa = new Adresa(
                            rs.getString("adresa_oras"),
                            rs.getString("adresa_strada"),
                            rs.getString("adresa_numar")
                    );

                    Restaurant restaurant = new Restaurant(
                            rs.getInt("restaurant_id"),
                            rs.getString("restaurant_nume"),
                            adresa
                    );

                    return Optional.of(restaurant);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la cautarea restaurantului: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Restaurant> findAll() {
        String sql = """
                SELECT
                    r.id AS restaurant_id,
                    r.nume AS restaurant_nume,
                    a.oras AS adresa_oras,
                    a.strada AS adresa_strada,
                    a.numar AS adresa_numar
                FROM restaurante r
                JOIN adrese a ON r.adresa_id = a.id
                """;

        List<Restaurant> restaurante = new ArrayList<>();

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

                Restaurant restaurant = new Restaurant(
                        rs.getInt("restaurant_id"),
                        rs.getString("restaurant_nume"),
                        adresa
                );

                restaurante.add(restaurant);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea restaurantelor: " + e.getMessage());
        }

        return restaurante;
    }

    @Override
    public void update(Restaurant restaurant) {
        String sql = """
                UPDATE restaurante
                SET nume = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, restaurant.getNume());
            stmt.setInt(2, restaurant.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea restaurantului: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM restaurante WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea restaurantului: " + e.getMessage());
        }
    }
}
