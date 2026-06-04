DROP TABLE IF EXISTS linii_comanda;
DROP TABLE IF EXISTS comenzi;
DROP TABLE IF EXISTS produse;
DROP TABLE IF EXISTS clienti;
DROP TABLE IF EXISTS furnizori;
DROP TABLE IF EXISTS categorii;

CREATE TABLE categorii (id INTEGER PRIMARY KEY, nume TEXT NOT NULL UNIQUE);
CREATE TABLE furnizori (id INTEGER PRIMARY KEY, nume TEXT NOT NULL, telefon TEXT NOT NULL);
CREATE TABLE clienti (id INTEGER PRIMARY KEY, nume TEXT NOT NULL, email TEXT NOT NULL UNIQUE, puncte INTEGER NOT NULL);
CREATE TABLE produse (
    cod TEXT PRIMARY KEY, nume TEXT NOT NULL, pret REAL NOT NULL, stoc INTEGER NOT NULL,
    categorie_id INTEGER NOT NULL, furnizor_id INTEGER NOT NULL,
    FOREIGN KEY (categorie_id) REFERENCES categorii(id),
    FOREIGN KEY (furnizor_id) REFERENCES furnizori(id)
);
CREATE TABLE comenzi (
    id INTEGER PRIMARY KEY, client_id INTEGER NOT NULL, data TEXT NOT NULL, total REAL NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clienti(id)
);
CREATE TABLE linii_comanda (
    comanda_id INTEGER NOT NULL, produs_cod TEXT NOT NULL, cantitate INTEGER NOT NULL, pret_unitar REAL NOT NULL,
    PRIMARY KEY (comanda_id, produs_cod),
    FOREIGN KEY (comanda_id) REFERENCES comenzi(id),
    FOREIGN KEY (produs_cod) REFERENCES produse(cod)
);
