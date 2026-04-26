# Platformă Food Delivery

## Descriere generală

Proiectul modelează o platformă de food delivery, asemănătoare cu Glovo, Bolt Food sau Tazz.  
Aplicația permite gestionarea restaurantelor, meniurilor, produselor, clienților, comenzilor și șoferilor.

Scopul proiectului este implementarea unui sistem orientat pe obiecte în Java, folosind clase, moștenire, colecții, servicii singleton și excepții custom.

---

## 1. Acțiuni / interogări posibile în sistem

1. Adaugă un restaurant nou în platformă.
2. Șterge un restaurant după id.
3. Caută un restaurant după id.
4. Caută un restaurant după nume.
5. Listează toate restaurantele disponibile.
6. Adaugă un client nou în platformă.
7. Șterge un client după id.
8. Caută un client după id.
9. Listează toți clienții.
10. Adaugă un produs în meniul unui restaurant.
11. Șterge un produs din meniul unui restaurant.
12. Listează meniul unui restaurant.
13. Plasează o comandă pentru un client.
14. Adaugă produse într-o comandă.
15. Atribuie un șofer unei comenzi.
16. Schimbă statusul unei comenzi.
17. Listează toate comenzile unui client.
18. Listează toate comenzile din platformă.
19. Listează comenzile sortate după valoarea totală.
20. Listează șoferii disponibili.

---

## 2. Tipuri de obiecte din domeniu

1. Persoană
2. Client
3. Angajat
4. Șofer
5. Restaurant
6. Meniu
7. Produs
8. Comandă
9. Adresă
10. Plată
11. StatusComandă

---

## 3. Clase principale propuse

### Persoana

Clasă abstractă care conține informații comune pentru persoanele din sistem.

Atribute:
- id
- nume
- telefon

Clase derivate:
- Client
- Angajat

---

### Client

Reprezintă utilizatorul care plasează comenzi.

Atribute:
- id
- nume
- telefon
- adresă
- istoric comenzi

---

### Angajat

Reprezintă un angajat al platformei.

Atribute:
- id
- nume
- telefon
- salariu

---

### Sofer

Reprezintă șoferul care livrează comenzile.

Atribute:
- id
- nume
- telefon
- salariu
- număr mașină
- disponibilitate

---

### Restaurant

Reprezintă un restaurant disponibil în platformă.

Atribute:
- id
- nume
- adresă
- meniu

---

### Meniu

Reprezintă meniul unui restaurant.

Atribute:
- produse

---

### Produs

Reprezintă un produs alimentar dintr-un meniu.

Atribute:
- id
- nume
- preț
- categorie

---

### Comanda

Reprezintă o comandă plasată de un client.

Atribute:
- id
- client
- restaurant
- produse
- șofer
- status comandă

---

### Adresa

Clasă imutabilă care reprezintă o adresă.

Atribute:
- oraș
- stradă
- număr

---

### Plata

Reprezintă informațiile despre plata unei comenzi.

Atribute:
- id
- sumă
- metodă plată

---

### StatusComanda

Enum care conține statusurile posibile ale unei comenzi:
- PLASATA
- ACCEPTATA
- IN_PREPARARE
- IN_LIVRARE
- LIVRATA
- ANULATA

---

## 4. Servicii propuse

1. RestaurantService
2. ClientService
3. SoferService
4. ComandaService

Fiecare serviciu va fi implementat folosind design pattern-ul Singleton și va expune operații de tip:
- adaugă
- șterge
- caută
- listează

---

## 5. Colecții folosite

În proiect vor fi folosite mai multe tipuri de colecții Java:

- List pentru liste de clienți, restaurante și comenzi
- Map pentru indexare după id
- TreeSet pentru produse sortate alfabetic în meniu

---

## 6. Excepții custom propuse

1. ClientNotFoundException
2. RestaurantNotFoundException
3. ProdusNotFoundException
4. SoferUnavailableException

Aceste excepții vor fi folosite pentru tratarea cazurilor speciale, de exemplu:
- client inexistent
- restaurant inexistent
- produs inexistent
- șofer indisponibil