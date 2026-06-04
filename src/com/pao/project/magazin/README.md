# Sistem de gestiune magazin

Aplicatie Java pentru administrarea produselor, stocurilor, categoriilor, furnizorilor,
clientilor, angajatilor si comenzilor.

## Actiuni

1. Adauga categorie.
2. Adauga furnizor.
3. Adauga produs.
4. Actualizeaza sau sterge produs.
5. Cauta produs dupa cod.
6. Listeaza produsele sortate.
7. Afiseaza produsele unei categorii.
8. Afiseaza produsele cu stoc mic.
9. Adauga si listeaza clienti.
10. Plaseaza comanda si genereaza rapoarte de vanzari.

## Obiecte

`Persoana`, `Client`, `Angajat`, `Manager`, `CodProdus`, `Categorie`, `Furnizor`,
`Produs`, `LinieComanda`, `Comanda`.

## Cerinte demonstrate

- Mostenire: `Persoana -> Angajat -> Manager` si `Persoana -> Client`.
- Polimorfism prin `getRol()`.
- Clasa imutabila `CodProdus`.
- Colectii `Map`, `List` si `TreeSet`.
- Servicii Singleton si exceptii custom.
- JDBC SQLite, repository generic si CRUD pentru patru entitati.
- Tranzactie explicita la plasarea unei comenzi: comanda, linii si actualizarea stocului.
- Trei rapoarte SQL cu `JOIN`.
- Audit thread-safe in `audit.csv`.

## Checklist Etapa I

- [x] Cel putin 10 actiuni si cel putin 8 tipuri de obiecte.
- [x] 10 clase de domeniu cu atribute private si metode `toString()`.
- [x] `equals()` si `hashCode()` in `CodProdus` si `Produs`.
- [x] Ierarhie pe minimum doua niveluri si clasa abstracta `Persoana`.
- [x] Clasa imutabila `CodProdus`.
- [x] Exceptii custom `ProdusInexistentException` si `StocInsuficientException`, aruncate si tratate.
- [x] Colectii `Map`, `List` si colectie sortata `TreeSet`.
- [x] Servicii Singleton cu operatii de adaugare, actualizare, stergere, cautare si listare.
- [x] `Main` demonstreaza actiunile sistemului.
- [x] Organizare in pachete `model`, `service`, `repository`, `exception` si `util`.

## Checklist Etapa II

- [x] `schema.sql` cu `DROP TABLE`, chei primare si chei externe.
- [x] `db.properties` si `DatabaseConnection` Singleton.
- [x] Interfata generica `Repository<T, ID>`.
- [x] CRUD complet pentru categorii, furnizori, clienti si produse.
- [x] Toate interogarile folosesc `PreparedStatement` si `try-with-resources`.
- [x] Tranzactie explicita cu `commit` si `rollback` la plasarea comenzii.
- [x] Trei interogari SQL cu `JOIN`.
- [x] `AuditService` Singleton, thread-safe, cu scriere append.

## Rulare

```powershell
javac -cp "lib\sqlite-jdbc-3.36.0.3.jar" -d output src\com\pao\project\magazin\*.java src\com\pao\project\magazin\model\*.java src\com\pao\project\magazin\service\*.java src\com\pao\project\magazin\exception\*.java src\com\pao\project\magazin\repository\*.java src\com\pao\project\magazin\util\*.java
java -cp "output;src;lib\sqlite-jdbc-3.36.0.3.jar" com.pao.project.magazin.Main
java -cp "output;src;lib\sqlite-jdbc-3.36.0.3.jar" com.pao.project.magazin.Main --menu
```
