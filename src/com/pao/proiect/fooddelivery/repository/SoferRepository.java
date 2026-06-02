package com.pao.proiect.fooddelivery.repository;

import com.pao.proiect.fooddelivery.model.Sofer;
import com.pao.proiect.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoferRepository implements Repository<Sofer, Integer> {

    @Override
    public void save(Sofer sofer) {
        String sql = """
                INSERT INTO soferi(id, nume, telefon, salariu, numar_masina, disponibil)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, sofer.getId());
            stmt.setString(2, sofer.getNume());
            stmt.setString(3, sofer.getTelefon());
            stmt.setDouble(4, sofer.getSalariu());
            stmt.setString(5, sofer.getNumarMasina());
            stmt.setInt(6, sofer.isDisponibil() ? 1 : 0);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la salvarea soferului: " + e.getMessage());
        }
    }

    @Override
    public Optional<Sofer> findById(Integer id) {
        String sql = """
                SELECT id, nume, telefon, salariu, numar_masina, disponibil
                FROM soferi
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Sofer sofer = new Sofer(
                            rs.getInt("id"),
                            rs.getString("nume"),
                            rs.getString("telefon"),
                            rs.getDouble("salariu"),
                            rs.getString("numar_masina")
                    );

                    sofer.setDisponibil(rs.getInt("disponibil") == 1);

                    return Optional.of(sofer);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la cautarea soferului: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Sofer> findAll() {
        String sql = """
                SELECT id, nume, telefon, salariu, numar_masina, disponibil
                FROM soferi
                """;

        List<Sofer> soferi = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sofer sofer = new Sofer(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("telefon"),
                        rs.getDouble("salariu"),
                        rs.getString("numar_masina")
                );

                sofer.setDisponibil(rs.getInt("disponibil") == 1);

                soferi.add(sofer);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea soferilor: " + e.getMessage());
        }

        return soferi;
    }

    public List<Sofer> findAllAvailable() {
        String sql = """
                SELECT id, nume, telefon, salariu, numar_masina, disponibil
                FROM soferi
                WHERE disponibil = 1
                """;

        List<Sofer> soferiDisponibili = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sofer sofer = new Sofer(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("telefon"),
                        rs.getDouble("salariu"),
                        rs.getString("numar_masina")
                );

                sofer.setDisponibil(rs.getInt("disponibil") == 1);

                soferiDisponibili.add(sofer);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea soferilor disponibili: " + e.getMessage());
        }

        return soferiDisponibili;
    }

    public Optional<Sofer> findFirstAvailable() {
        String sql = """
                SELECT id, nume, telefon, salariu, numar_masina, disponibil
                FROM soferi
                WHERE disponibil = 1
                FETCH FIRST 1 ROWS ONLY
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Sofer sofer = new Sofer(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("telefon"),
                        rs.getDouble("salariu"),
                        rs.getString("numar_masina")
                );

                sofer.setDisponibil(rs.getInt("disponibil") == 1);

                return Optional.of(sofer);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la cautarea unui sofer disponibil: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void update(Sofer sofer) {
        String sql = """
                UPDATE soferi
                SET nume = ?, telefon = ?, salariu = ?, numar_masina = ?, disponibil = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, sofer.getNume());
            stmt.setString(2, sofer.getTelefon());
            stmt.setDouble(3, sofer.getSalariu());
            stmt.setString(4, sofer.getNumarMasina());
            stmt.setInt(5, sofer.isDisponibil() ? 1 : 0);
            stmt.setInt(6, sofer.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea soferului: " + e.getMessage());
        }
    }

    public void updateDisponibilitate(int soferId, boolean disponibil) {
        String sql = """
                UPDATE soferi
                SET disponibil = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, disponibil ? 1 : 0);
            stmt.setInt(2, soferId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea disponibilitatii soferului: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM soferi WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea soferului: " + e.getMessage());
        }
    }
}
