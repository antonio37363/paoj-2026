package com.pao.proiect.fooddelivery.repository;

import com.pao.proiect.fooddelivery.model.Client;
import com.pao.proiect.fooddelivery.model.Comanda;
import com.pao.proiect.fooddelivery.model.Plata;
import com.pao.proiect.fooddelivery.model.Produs;
import com.pao.proiect.fooddelivery.model.Restaurant;
import com.pao.proiect.fooddelivery.model.Sofer;
import com.pao.proiect.fooddelivery.model.StatusComanda;
import com.pao.proiect.fooddelivery.util.DatabaseConnection;
import com.pao.proiect.fooddelivery.dto.ComandaClientTotalDTO;

import java.sql.*;
import java.util.*;

// Repository pentru gestionarea comenzilor, inclusiv salvarea, actualizarea, ștergerea și căutarea comenzilor în baza de date.
public class ComandaRepository implements Repository<Comanda, Integer> {

    private final ClientRepository clientRepository = new ClientRepository();
    private final RestaurantRepository restaurantRepository = new RestaurantRepository();
    private final ProdusRepository produsRepository = new ProdusRepository();
    private final SoferRepository soferRepository = new SoferRepository();

    @Override
    public void save(Comanda comanda) {
        String insertPlataSql = """
                INSERT INTO plati(id, suma, metoda_plata, efectuata)
                VALUES (?, ?, ?, ?)
                """;

        String insertComandaSql = """
                INSERT INTO comenzi(id, client_id, restaurant_id, sofer_id, plata_id, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String insertComandaProdusSql = """
                INSERT INTO comanda_produse(comanda_id, produs_id, cantitate)
                VALUES (?, ?, ?)
                """;

        String updateSoferSql = """
                UPDATE soferi
                SET disponibil = 0
                WHERE id = ?
                """;

        Connection connection = DatabaseConnection.getInstance().getConnection();

        // Folosim tranzacții pentru a asigura integritatea datelor în cazul în care una dintre operațiuni eșuează.
        try {
            connection.setAutoCommit(false);

            Plata plata = comanda.getPlata();

            // Dacă comanda are o plată asociată, o salvăm mai întâi pentru a obține ID-ul necesar pentru tabela comenzi.
            if (plata != null) {
                try (PreparedStatement plataStmt = connection.prepareStatement(insertPlataSql)) {
                    plataStmt.setInt(1, plata.getId());
                    plataStmt.setDouble(2, plata.getSuma());
                    plataStmt.setString(3, plata.getMetodaPlata());
                    plataStmt.setInt(4, plata.isEfectuata() ? 1 : 0);

                    plataStmt.executeUpdate();
                }
            }

            // Salvăm comanda, referențiind ID-urile clientului, restaurantului, șoferului și plății (dacă există).
            try (PreparedStatement comandaStmt = connection.prepareStatement(insertComandaSql)) {
                comandaStmt.setInt(1, comanda.getId());
                comandaStmt.setInt(2, comanda.getClient().getId());
                comandaStmt.setInt(3, comanda.getRestaurant().getId());

                if (comanda.getSofer() != null) {
                    comandaStmt.setInt(4, comanda.getSofer().getId());
                } else {
                    comandaStmt.setNull(4, Types.INTEGER);
                }

                if (plata != null) {
                    comandaStmt.setInt(5, plata.getId());
                } else {
                    comandaStmt.setNull(5, Types.INTEGER);
                }

                comandaStmt.setString(6, comanda.getStatus().name());

                comandaStmt.executeUpdate();
            }

            Map<Integer, Integer> cantitati = new HashMap<>();

            // Calculăm cantitatea fiecărui produs din comandă pentru a le salva în tabela de legătură comanda_produse.
            for (Produs produs : comanda.getProduse()) {
                int produsId = produs.getId();
                cantitati.put(produsId, cantitati.getOrDefault(produsId, 0) + 1);
            }

            // Salvăm legătura dintre comanda și produse, inclusiv cantitatea fiecărui produs.
            try (PreparedStatement produsStmt = connection.prepareStatement(insertComandaProdusSql)) {
                for (Map.Entry<Integer, Integer> entry : cantitati.entrySet()) {
                    produsStmt.setInt(1, comanda.getId());
                    produsStmt.setInt(2, entry.getKey());
                    produsStmt.setInt(3, entry.getValue());

                    produsStmt.executeUpdate();
                }
            }

            // Dacă comanda are un șofer asociat, îl marcăm ca indisponibil în baza de date.
            if (comanda.getSofer() != null) {
                try (PreparedStatement soferStmt = connection.prepareStatement(updateSoferSql)) {
                    soferStmt.setInt(1, comanda.getSofer().getId());
                    soferStmt.executeUpdate();
                }
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println("Eroare la rollback: " + rollbackException.getMessage());
            }

            System.out.println("Eroare la salvarea comenzii: " + e.getMessage());

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Eroare la resetarea autoCommit: " + e.getMessage());
            }
        }
    }

    // Căutăm o comandă după ID și construim obiectul Comanda complet, inclusiv clientul, restaurantul, produsele, șoferul și plata asociate.
    @Override
    public Optional<Comanda> findById(Integer id) {
        String sql = """
                SELECT id, client_id, restaurant_id, sofer_id, plata_id, status
                FROM comenzi
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(buildComandaFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la cautarea comenzii: " + e.getMessage());
        }

        return Optional.empty();
    }


    // Căutăm toate comenzile din baza de date și construim o listă de obiecte Comanda complete pentru fiecare înregistrare.
    @Override
    public List<Comanda> findAll() {
        String sql = """
                SELECT id, client_id, restaurant_id, sofer_id, plata_id, status
                FROM comenzi
                """;

        List<Comanda> comenzi = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Comanda comanda = buildComandaFromResultSet(rs);
                comenzi.add(comanda);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea comenzilor: " + e.getMessage());
        }

        return comenzi;
    }

    // Căutăm comenzile unui anumit client după ID-ul clientului și construim o listă de obiecte Comanda pentru fiecare comandă găsită.
    public List<Comanda> findByClientId(int clientId) {
        String sql = """
                SELECT id, client_id, restaurant_id, sofer_id, plata_id, status
                FROM comenzi
                WHERE client_id = ?
                """;

        List<Comanda> comenzi = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comanda comanda = buildComandaFromResultSet(rs);
                    comenzi.add(comanda);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea comenzilor clientului: " + e.getMessage());
        }

        return comenzi;
    }

    // Actualizăm o comandă existentă în baza de date, permițând modificarea șoferului, plății și statusului comenzii.
    @Override
    public void update(Comanda comanda) {
        String sql = """
                UPDATE comenzi
                SET sofer_id = ?, plata_id = ?, status = ?
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            if (comanda.getSofer() != null) {
                stmt.setInt(1, comanda.getSofer().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            if (comanda.getPlata() != null) {
                stmt.setInt(2, comanda.getPlata().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setString(3, comanda.getStatus().name());
            stmt.setInt(4, comanda.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea comenzii: " + e.getMessage());
        }
    }

    // Ștergem o comandă din baza de date după ID, eliminând înregistrarea corespunzătoare din tabela comenzi.
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM comenzi WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la stergerea comenzii: " + e.getMessage());
        }
    }

    // Metodă auxiliară pentru a construi un obiect Comanda complet dintr-un ResultSet, citind toate informațiile necesare despre client, restaurant, produse, șofer și plată.
    private Comanda buildComandaFromResultSet(ResultSet rs) throws SQLException {
        int comandaId = rs.getInt("id");
        int clientId = rs.getInt("client_id");
        int restaurantId = rs.getInt("restaurant_id");

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new SQLException("Clientul comenzii nu exista."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new SQLException("Restaurantul comenzii nu exista."));

        Comanda comanda = new Comanda(comandaId, client, restaurant);

        List<Produs> produse = findProduseByComandaId(comandaId);
        for (Produs produs : produse) {
            comanda.adaugaProdus(produs);
        }

        int soferId = rs.getInt("sofer_id");
        if (!rs.wasNull()) {
            Optional<Sofer> soferOptional = soferRepository.findById(soferId);
            soferOptional.ifPresent(comanda::atribuieSofer);
        }

        String status = rs.getString("status");
        comanda.schimbaStatus(StatusComanda.valueOf(status));

        int plataId = rs.getInt("plata_id");
        if (!rs.wasNull()) {
            Optional<Plata> plataOptional = findPlataById(plataId);

            if (plataOptional.isPresent()) {
                Plata plata = plataOptional.get();

                comanda.genereazaPlata(plata.getId(), plata.getMetodaPlata());
                comanda.getPlata().setEfectuata(plata.isEfectuata());
            }
        }

        return comanda;
    }

    // Căutăm produsele asociate unei comenzi după ID-ul comenzii, citind din tabela de legătură comanda_produse și construind o listă de obiecte Produs pentru fiecare produs găsit.
    private List<Produs> findProduseByComandaId(int comandaId) {
        String sql = """
                SELECT produs_id, cantitate
                FROM comanda_produse
                WHERE comanda_id = ?
                """;

        List<Produs> produse = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, comandaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int produsId = rs.getInt("produs_id");
                    int cantitate = rs.getInt("cantitate");

                    Optional<Produs> produsOptional = produsRepository.findById(produsId);

                    if (produsOptional.isPresent()) {
                        Produs produs = produsOptional.get();

                        for (int i = 0; i < cantitate; i++) {
                            produse.add(produs);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea produselor comenzii: " + e.getMessage());
        }

        return produse;
    }

    // Căutăm o plată după ID și construim un obiect Plata complet, citind informațiile despre sumă, metoda de plată și dacă plata a fost efectuată sau nu.
    private Optional<Plata> findPlataById(int plataId) {
        String sql = """
                SELECT id, suma, metoda_plata, efectuata
                FROM plati
                WHERE id = ?
                """;

        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, plataId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Plata plata = new Plata(
                            rs.getInt("id"),
                            rs.getDouble("suma"),
                            rs.getString("metoda_plata")
                    );

                    plata.setEfectuata(rs.getInt("efectuata") == 1);

                    return Optional.of(plata);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea platii: " + e.getMessage());
        }

        return Optional.empty();
    }

    // Căutăm comenzile unui anumit client după ID-ul clientului și calculăm totalul fiecărei comenzi, returnând o listă de obiecte ComandaClientTotalDTO care conțin informațiile despre comandă și totalul acesteia.
    public List<ComandaClientTotalDTO> findComenziClientCuTotal(int clientId) {
        // Interogare SQL care calculează totalul fiecărei comenzi pentru un anumit client, alături de informațiile despre comandă, client și restaurant.
        String sql = """
            SELECT
                c.id AS comanda_id,
                cl.nume AS client_nume,
                r.nume AS restaurant_nume,
                c.status AS status,
                SUM(cp.cantitate * p.pret) AS total
            FROM comenzi c
            JOIN clienti cl ON c.client_id = cl.id
            JOIN restaurante r ON c.restaurant_id = r.id
            JOIN comanda_produse cp ON c.id = cp.comanda_id
            JOIN produse p ON cp.produs_id = p.id
            WHERE cl.id = ?
            GROUP BY c.id, cl.nume, r.nume, c.status
            """;

        List<ComandaClientTotalDTO> rezultate = new ArrayList<>();

        // Executăm interogarea și construim o listă de obiecte ComandaClientTotalDTO pentru fiecare comandă găsită, care conțin ID-ul comenzii, numele clientului, numele restaurantului, statusul comenzii și totalul acesteia.
        try (PreparedStatement stmt = DatabaseConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ComandaClientTotalDTO dto = new ComandaClientTotalDTO(
                            rs.getInt("comanda_id"),
                            rs.getString("client_nume"),
                            rs.getString("restaurant_nume"),
                            rs.getString("status"),
                            rs.getDouble("total")
                    );

                    rezultate.add(dto);
                }
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea comenzilor clientului cu total: " + e.getMessage());
        }

        return rezultate;
    }
}
