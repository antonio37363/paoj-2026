package com.pao.proiect.fooddelivery;

import com.pao.proiect.fooddelivery.exception.ClientNotFoundException;
import com.pao.proiect.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.proiect.fooddelivery.exception.SoferUnavailableException;
import com.pao.proiect.fooddelivery.model.*;
import com.pao.proiect.fooddelivery.service.ClientService;
import com.pao.proiect.fooddelivery.service.ComandaService;
import com.pao.proiect.fooddelivery.service.RestaurantService;
import com.pao.proiect.fooddelivery.service.SoferService;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final RestaurantService restaurantService = RestaurantService.getInstance();
    private static final ClientService clientService = ClientService.getInstance();
    private static final SoferService soferService = SoferService.getInstance();
    private static final ComandaService comandaService = ComandaService.getInstance();

    private static int nextRestaurantId = 1;
    private static int nextClientId = 1;
    private static int nextSoferId = 1;
    private static int nextProdusId = 1;
    private static int nextComandaId = 1;
    private static int nextPlataId = 1;

    public static void main(String[] args) {
        incarcaDateInitiale();

        boolean ruleaza = true;

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

    private static void afiseazaMeniuPrincipal() {
        System.out.println("\n=== Platforma Food Delivery ===");
        System.out.println("1. Profil Owner platforma");
        System.out.println("2. Profil Client");
        System.out.println("0. Iesire");
    }

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

    private static void adaugaRestaurantInteractively() {
        System.out.println("\n--- Adauga restaurant ---");

        String nume = citesteText("Nume restaurant: ");

        String oras = citesteText("Oras: ");
        String strada = citesteText("Strada: ");
        String numar = citesteText("Numar: ");

        Adresa adresa = new Adresa(oras, strada, numar);
        Restaurant restaurant = new Restaurant(nextRestaurantId++, nume, adresa);

        restaurantService.adaugaRestaurant(restaurant);

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
            restaurant.getMeniu().adaugaProdus(produs);

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

        System.out.println("Client adaugat cu succes:");
        afiseazaClient(client);
    }

    private static void veziMeniuRestaurantInteractively() {
        System.out.println("\n--- Vezi meniul unui restaurant ---");

        int restaurantId = citesteInt("Id restaurant: ");

        try {
            Restaurant restaurant = restaurantService.cautaRestaurantDupaId(restaurantId);
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

            System.out.println("Comanda finalizata:");
            afiseazaComanda(comanda);

        } catch (ClientNotFoundException | RestaurantNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

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

    private static void incarcaDateInitiale() {
        Adresa adresaRestaurant = new Adresa("Bucuresti", "Strada Victoriei", "10");
        Restaurant restaurant = new Restaurant(nextRestaurantId++, "Pizza Napoli", adresaRestaurant);

        Produs pizza = new Produs(nextProdusId++, "Pizza Margherita", 35.0, "Pizza");
        Produs paste = new Produs(nextProdusId++, "Paste Carbonara", 38.0, "Paste");
        Produs cola = new Produs(nextProdusId++, "Cola", 8.0, "Bautura");

        restaurant.getMeniu().adaugaProdus(pizza);
        restaurant.getMeniu().adaugaProdus(paste);
        restaurant.getMeniu().adaugaProdus(cola);

        restaurantService.adaugaRestaurant(restaurant);

        Adresa adresaClient = new Adresa("Bucuresti", "Strada Libertatii", "7");
        Client client = new Client(nextClientId++, "Antonio", "0712345678", adresaClient);
        clientService.adaugaClient(client);

        Sofer sofer = new Sofer(nextSoferId++, "Andrei", "0722222222", 3500.0, "B-123-ABC");
        soferService.adaugaSofer(sofer);
    }








    private static void afiseazaRestaurante() {
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