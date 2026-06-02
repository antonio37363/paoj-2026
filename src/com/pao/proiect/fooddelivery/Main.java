package com.pao.proiect.fooddelivery;

import com.pao.proiect.fooddelivery.exception.ClientNotFoundException;
import com.pao.proiect.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.proiect.fooddelivery.exception.SoferUnavailableException;
import com.pao.proiect.fooddelivery.model.*;
import com.pao.proiect.fooddelivery.service.ClientService;
import com.pao.proiect.fooddelivery.service.ComandaService;
import com.pao.proiect.fooddelivery.service.RestaurantService;
import com.pao.proiect.fooddelivery.service.SoferService;
import com.pao.proiect.fooddelivery.service.AuditService;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // Serviciile singleton pentru gestionarea datelor
    private static final RestaurantService restaurantService = RestaurantService.getInstance();
    private static final ClientService clientService = ClientService.getInstance();
    private static final SoferService soferService = SoferService.getInstance();
    private static final ComandaService comandaService = ComandaService.getInstance();
    private static final AuditService auditService = AuditService.getInstance();

    private static int nextRestaurantId = 1;
    private static int nextClientId = 1;
    private static int nextSoferId = 1;
    private static int nextProdusId = 1;
    private static int nextComandaId = 1;
    private static int nextPlataId = 1;

    public static void main(String[] args) {
        sincronizeazaNextIdsCuBazaDeDate();
        incarcaDateInitiale();
        sincronizeazaNextIdsCuBazaDeDate();

        boolean ruleaza = true;

        // Bucla principala a aplicatiei
        while (ruleaza) {
            afiseazaMeniuPrincipal();

            int optiune = citesteInt("Alege optiunea: ");

            switch (optiune) {
                case 1:
                    meniuOwner();
                    break;
                case 2:
                    meniuClient();
                    break;
                case 0:
                    ruleaza = false;
                    System.out.println("Aplicatia s-a inchis.");
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    // Metode pentru afisarea meniurilor, citirea input-ului si gestionarea interactiunii cu utilizatorul
    private static void afiseazaMeniuPrincipal() {
        System.out.println("\n=== Platforma Food Delivery ===");
        System.out.println("1. Profil Owner platforma");
        System.out.println("2. Profil Client");
        System.out.println("0. Iesire");
    }

    // Meniul pentru owner si client, precum si metodele pentru adaugarea restaurantelor, produselor, soferilor, clientilor, plasarea comenzilor etc.
    private static void meniuOwner() {
        boolean inapoi = false;

        while (!inapoi) {
            System.out.println("\n=== Meniu Owner ===");
            System.out.println("1. Adauga restaurant");
            System.out.println("2. Adauga produs in meniul unui restaurant");
            System.out.println("3. Adauga sofer");
            System.out.println("4. Listeaza restaurante");
            System.out.println("5. Listeaza soferi");
            System.out.println("6. Listeaza comenzi");
            System.out.println("0. Inapoi");

            int optiune = citesteInt("Alege optiunea: ");

            switch (optiune) {
                case 1:
                    adaugaRestaurantInteractively();
                    break;
                case 2:
                    adaugaProdusInRestaurantInteractively();
                    break;
                case 3:
                    adaugaSoferInteractively();
                    break;
                case 4:
                    afiseazaRestaurante();
                    break;
                case 5:
                    afiseazaSoferi();
                    break;
                case 6:
                    afiseazaComenzi();
                    break;
                case 0:
                    inapoi = true;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    // Meniul pentru client, cu optiuni pentru crearea contului, vizualizarea restaurantelor, plasarea comenzilor etc.
    private static void meniuClient() {
        boolean inapoi = false;

        while (!inapoi) {
            System.out.println("\n=== Meniu Client ===");
            System.out.println("1. Creeaza cont client");
            System.out.println("2. Listeaza restaurante");
            System.out.println("3. Vezi meniul unui restaurant");
            System.out.println("4. Plaseaza comanda");
            System.out.println("5. Listeaza clientii");
            System.out.println("0. Inapoi");

            int optiune = citesteInt("Alege optiunea: ");

            switch (optiune) {
                case 1:
                    adaugaClientInteractively();
                    break;
                case 2:
                    afiseazaRestaurante();
                    break;
                case 3:
                    veziMeniuRestaurantInteractively();
                    break;
                case 4:
                    plaseazaComandaInteractively();
                    break;
                case 5:
                    afiseazaClienti();
                    break;
                case 0:
                    inapoi = true;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }



    // Metodele pentru interactiunea cu utilizatorul, adaugarea de restaurante, produse, soferi, clienti, plasarea comenzilor etc.
    private static void adaugaRestaurantInteractively() {
        System.out.println("\n--- Adauga restaurant ---");

        String nume = citesteText("Nume restaurant: ");

        String oras = citesteText("Oras: ");
        String strada = citesteText("Strada: ");
        String numar = citesteText("Numar: ");

        Adresa adresa = new Adresa(oras, strada, numar);
        Restaurant restaurant = new Restaurant(nextRestaurantId++, nume, adresa);

        restaurantService.adaugaRestaurant(restaurant);
        auditService.logAction("adauga_restaurant");

        System.out.println("Restaurant adaugat cu succes:");
        afiseazaRestaurant(restaurant);
    }

    private static void adaugaProdusInRestaurantInteractively() {
        System.out.println("\n--- Adauga produs in restaurant ---");

        int restaurantId = citesteInt("Id restaurant: ");

        try {
            Restaurant restaurant = restaurantService.cautaRestaurantDupaId(restaurantId);

            String numeProdus = citesteText("Nume produs: ");
            double pret = citesteDouble("Pret produs: ");
            String categorie = citesteText("Categorie: ");

            Produs produs = new Produs(nextProdusId++, numeProdus, pret, categorie);
            restaurantService.adaugaProdusInRestaurant(restaurantId, produs);
            auditService.logAction("adauga_produs_in_restaurant");

            System.out.println("Produs adaugat cu succes in meniul restaurantului " + restaurant.getNume());
            afiseazaProdus(produs);

        } catch (RestaurantNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaSoferInteractively() {
        System.out.println("\n--- Adauga sofer ---");

        String nume = citesteText("Nume sofer: ");
        String telefon = citesteText("Telefon: ");
        double salariu = citesteDouble("Salariu: ");
        String numarMasina = citesteText("Numar masina: ");

        Sofer sofer = new Sofer(nextSoferId++, nume, telefon, salariu, numarMasina);

        soferService.adaugaSofer(sofer);
        auditService.logAction("adauga_sofer");

        System.out.println("Sofer adaugat cu succes:");
        afiseazaSofer(sofer);
    }

    private static void adaugaClientInteractively() {
        System.out.println("\n--- Creeaza cont client ---");

        String nume = citesteText("Nume client: ");
        String telefon = citesteText("Telefon: ");

        String oras = citesteText("Oras: ");
        String strada = citesteText("Strada: ");
        String numar = citesteText("Numar: ");

        Adresa adresa = new Adresa(oras, strada, numar);
        Client client = new Client(nextClientId++, nume, telefon, adresa);

        clientService.adaugaClient(client);
        auditService.logAction("adauga_client");

        System.out.println("Client adaugat cu succes:");
        afiseazaClient(client);
    }

    private static void veziMeniuRestaurantInteractively() {
        System.out.println("\n--- Vezi meniul unui restaurant ---");

        int restaurantId = citesteInt("Id restaurant: ");

        try {
            Restaurant restaurant = restaurantService.cautaRestaurantDupaId(restaurantId);
            auditService.logAction("vezi_meniu_restaurant");
            afiseazaMeniuRestaurant(restaurant);

        } catch (RestaurantNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void plaseazaComandaInteractively() {
        System.out.println("\n--- Plaseaza comanda ---");

        int clientId = citesteInt("Id client: ");
        int restaurantId = citesteInt("Id restaurant: ");

        try {
            Client client = clientService.cautaClientDupaId(clientId);
            Restaurant restaurant = restaurantService.cautaRestaurantDupaId(restaurantId);

            Comanda comanda = comandaService.plaseazaComanda(nextComandaId++, client, restaurant);

            boolean adaugaProduse = true;

            while (adaugaProduse) {
                afiseazaMeniuRestaurant(restaurant);

                String numeProdus = citesteText("Scrie numele produsului dorit: ");

                Produs produs = restaurant.getMeniu().cautaProdusDupaNume(numeProdus);

                if (produs != null) {
                    comanda.adaugaProdus(produs);
                    System.out.println("Produs adaugat in comanda.");
                } else {
                    System.out.println("Produsul nu exista in meniu.");
                }

                String raspuns = citesteText("Mai adaugi produse? da/nu: ");

                if (!raspuns.equalsIgnoreCase("da")) {
                    adaugaProduse = false;
                }
            }

            if (comanda.getProduse().isEmpty()) {
                System.out.println("Comanda nu are produse. Nu poate fi finalizata.");
                comandaService.stergeComandaDupaId(comanda.getId());
                return;
            }

            String metodaPlata = citesteText("Metoda plata CARD/CASH: ");
            comanda.genereazaPlata(nextPlataId++, metodaPlata);

            try {
                Sofer soferDisponibil = soferService.cautaSoferDisponibil();
                comandaService.atribuieSofer(comanda, soferDisponibil);
            } catch (SoferUnavailableException e) {
                System.out.println("Comanda a fost plasata, dar nu exista momentan sofer disponibil.");
            }

            comandaService.salveazaComanda(comanda);
            auditService.logAction("plaseaza_comanda");

            System.out.println("Comanda finalizata:");
            afiseazaComanda(comanda);

        } catch (ClientNotFoundException | RestaurantNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }





    // Metodele pentru citirea input-ului de la utilizator

    private static int citesteInt(String mesaj) {
        System.out.print(mesaj);

        while (!scanner.hasNextInt()) {
            System.out.println("Te rog introdu un numar intreg.");
            scanner.nextLine();
            System.out.print(mesaj);
        }

        int valoare = scanner.nextInt();
        scanner.nextLine();

        return valoare;
    }

    private static double citesteDouble(String mesaj) {
        System.out.print(mesaj);

        while (!scanner.hasNextDouble()) {
            System.out.println("Te rog introdu un numar valid.");
            scanner.nextLine();
            System.out.print(mesaj);
        }

        double valoare = scanner.nextDouble();
        scanner.nextLine();

        return valoare;
    }

    private static String citesteText(String mesaj) {
        System.out.print(mesaj);
        return scanner.nextLine();
    }



    // Metoda pentru incarcarea datelor initiale in aplicatie

    private static void incarcaDateInitiale() {
        Restaurant restaurant = cautaRestaurantInitial("Pizza Napoli");

        if (restaurant == null) {
            Adresa adresaRestaurant = new Adresa("Bucuresti", "Strada Victoriei", "10");
            restaurant = new Restaurant(nextRestaurantId++, "Pizza Napoli", adresaRestaurant);
            restaurantService.adaugaRestaurant(restaurant);
        }

        adaugaProdusInitialDacaLipseste(restaurant, "Pizza Margherita", 35.0, "Pizza");
        adaugaProdusInitialDacaLipseste(restaurant, "Paste Carbonara", 38.0, "Paste");
        adaugaProdusInitialDacaLipseste(restaurant, "Cola", 8.0, "Bautura");

        if (!existaClientInitial("Antonio", "0712345678")) {
            Adresa adresaClient = new Adresa("Bucuresti", "Strada Libertatii", "7");
            Client client = new Client(nextClientId++, "Antonio", "0712345678", adresaClient);
            clientService.adaugaClient(client);
        }

        if (!existaSoferInitial("Andrei", "0722222222")) {
            Sofer sofer = new Sofer(nextSoferId++, "Andrei", "0722222222", 3500.0, "B-123-ABC");
            soferService.adaugaSofer(sofer);
        }
    }





    // Metodele pentru cautarea datelor initiale in baza de date si adaugarea acestora daca nu exista, precum si sincronizarea nextId-urilor cu baza de date


    private static Restaurant cautaRestaurantInitial(String nume) {
        for (Restaurant restaurant : restaurantService.listeazaRestaurante()) {
            if (restaurant.getNume().equalsIgnoreCase(nume)) {
                return restaurant;
            }
        }

        return null;
    }

    private static void adaugaProdusInitialDacaLipseste(
            Restaurant restaurant,
            String numeProdus,
            double pret,
            String categorie
    ) {
        if (restaurant.getMeniu().cautaProdusDupaNume(numeProdus) != null) {
            return;
        }

        Produs produs = new Produs(nextProdusId++, numeProdus, pret, categorie);

        try {
            restaurantService.adaugaProdusInRestaurant(restaurant.getId(), produs);
        } catch (RestaurantNotFoundException e) {
            System.out.println("Nu s-a putut incarca produsul initial: " + e.getMessage());
        }
    }

    private static boolean existaClientInitial(String nume, String telefon) {
        for (Client client : clientService.listeazaClienti()) {
            if (client.getNume().equalsIgnoreCase(nume) && client.getTelefon().equals(telefon)) {
                return true;
            }
        }

        return false;
    }

    private static boolean existaSoferInitial(String nume, String telefon) {
        for (Sofer sofer : soferService.listeazaSoferi()) {
            if (sofer.getNume().equalsIgnoreCase(nume) && sofer.getTelefon().equals(telefon)) {
                return true;
            }
        }

        return false;
    }

    private static void sincronizeazaNextIdsCuBazaDeDate() {
        int maxRestaurantId = 0;
        int maxProdusId = 0;
        int maxClientId = 0;
        int maxSoferId = 0;
        int maxComandaId = 0;
        int maxPlataId = 0;

        for (Restaurant restaurant : restaurantService.listeazaRestaurante()) {
            maxRestaurantId = Math.max(maxRestaurantId, restaurant.getId());

            for (Produs produs : restaurant.getMeniu().getProduse()) {
                maxProdusId = Math.max(maxProdusId, produs.getId());
            }
        }

        for (Client client : clientService.listeazaClienti()) {
            maxClientId = Math.max(maxClientId, client.getId());
        }

        for (Sofer sofer : soferService.listeazaSoferi()) {
            maxSoferId = Math.max(maxSoferId, sofer.getId());
        }

        for (Comanda comanda : comandaService.listeazaComenzi()) {
            maxComandaId = Math.max(maxComandaId, comanda.getId());

            if (comanda.getPlata() != null) {
                maxPlataId = Math.max(maxPlataId, comanda.getPlata().getId());
            }
        }

        nextRestaurantId = Math.max(nextRestaurantId, maxRestaurantId + 1);
        nextProdusId = Math.max(nextProdusId, maxProdusId + 1);
        nextClientId = Math.max(nextClientId, maxClientId + 1);
        nextSoferId = Math.max(nextSoferId, maxSoferId + 1);
        nextComandaId = Math.max(nextComandaId, maxComandaId + 1);
        nextPlataId = Math.max(nextPlataId, maxPlataId + 1);
    }





    

    // Metodele pentru afisarea restaurantelor, soferilor, clientilor, comenzilor, meniurilor etc.

    private static void afiseazaRestaurante() {
        auditService.logAction("listeaza_restaurante");

        System.out.println("\n--- Restaurante disponibile ---");

        if (restaurantService.listeazaRestaurante().isEmpty()) {
            System.out.println("Nu exista restaurante in platforma.");
            return;
        }

        for (Restaurant restaurant : restaurantService.listeazaRestaurante()) {
            afiseazaRestaurant(restaurant);
        }
    }

    private static void afiseazaSoferi() {
        auditService.logAction("listeaza_soferi");

        System.out.println("\n--- Soferi inregistrati ---");

        if (soferService.listeazaSoferi().isEmpty()) {
            System.out.println("Nu exista soferi in platforma.");
            return;
        }

        for (Sofer sofer : soferService.listeazaSoferi()) {
            afiseazaSofer(sofer);
        }
    }

    private static void afiseazaClienti() {
        auditService.logAction("listeaza_clienti");

        System.out.println("\n--- Clienti inregistrati ---");

        if (clientService.listeazaClienti().isEmpty()) {
            System.out.println("Nu exista clienti in platforma.");
            return;
        }

        for (Client client : clientService.listeazaClienti()) {
            afiseazaClient(client);
        }
    }

    private static void afiseazaComenzi() {
        auditService.logAction("listeaza_comenzi");

        System.out.println("\n--- Comenzi inregistrate ---");

        if (comandaService.listeazaComenzi().isEmpty()) {
            System.out.println("Nu exista comenzi in platforma.");
            return;
        }

        for (Comanda comanda : comandaService.listeazaComenzi()) {
            afiseazaComanda(comanda);
        }
    }


    private static void afiseazaRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            System.out.println("Restaurant invalid.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("ID: " + restaurant.getId());
        System.out.println("Nume: " + restaurant.getNume());
        System.out.println("Adresa: " + restaurant.getAdresa());
        System.out.println("------------------------------");
    }


    private static void afiseazaProdus(Produs produs) {
        if (produs == null) {
            System.out.println("Produs invalid.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("ID: " + produs.getId());
        System.out.println("Nume: " + produs.getNume());
        System.out.println("Pret: " + produs.getPret() + " lei");
        System.out.println("Categorie: " + produs.getCategorie());
        System.out.println("------------------------------");
    }

    private static void afiseazaMeniuRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            System.out.println("Restaurant invalid.");
            return;
        }

        System.out.println("\n--- Meniu: " + restaurant.getNume() + " ---");

        if (restaurant.getMeniu().getProduse().isEmpty()) {
            System.out.println("Restaurantul nu are produse in meniu.");
            return;
        }

        for (Produs produs : restaurant.getMeniu().getProduse()) {
            System.out.println("ID: " + produs.getId()
                    + " | " + produs.getNume()
                    + " | " + produs.getPret() + " lei"
                    + " | " + produs.getCategorie());
        }
    }

    private static void afiseazaClient(Client client) {
        if (client == null) {
            System.out.println("Client invalid.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("ID: " + client.getId());
        System.out.println("Nume: " + client.getNume());
        System.out.println("Telefon: " + client.getTelefon());
        System.out.println("Adresa: " + client.getAdresa());
        System.out.println("------------------------------");
    }


    private static void afiseazaSofer(Sofer sofer) {
        if (sofer == null) {
            System.out.println("Sofer invalid.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("ID: " + sofer.getId());
        System.out.println("Nume: " + sofer.getNume());
        System.out.println("Telefon: " + sofer.getTelefon());
        System.out.println("Salariu: " + sofer.getSalariu() + " lei");
        System.out.println("Numar masina: " + sofer.getNumarMasina());

        if (sofer.isDisponibil()) {
            System.out.println("Status: disponibil");
        } else {
            System.out.println("Status: indisponibil");
        }

        System.out.println("------------------------------");
    }


    private static void afiseazaComanda(Comanda comanda) {
        if (comanda == null) {
            System.out.println("Comanda invalida.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("ID comanda: " + comanda.getId());
        System.out.println("Client: " + comanda.getClient().getNume());
        System.out.println("Restaurant: " + comanda.getRestaurant().getNume());
        System.out.println("Status: " + comanda.getStatus());

        if (comanda.getSofer() != null) {
            System.out.println("Sofer: " + comanda.getSofer().getNume());
        } else {
            System.out.println("Sofer: neatribuit");
        }

        System.out.println("Produse:");

        if (comanda.getProduse().isEmpty()) {
            System.out.println("  Nu exista produse in comanda.");
        } else {
            for (Produs produs : comanda.getProduse()) {
                System.out.println("  - " + produs.getNume() + " | " + produs.getPret() + " lei");
            }
        }

        System.out.println("Total: " + comanda.calculeazaTotal() + " lei");

        if (comanda.getPlata() != null) {
            System.out.println("Metoda plata: " + comanda.getPlata().getMetodaPlata());
            System.out.println("Plata efectuata: " + comanda.getPlata().isEfectuata());
        } else {
            System.out.println("Plata: necreata");
        }

        System.out.println("------------------------------");
    }



}
