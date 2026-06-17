-- Creazione database
CREATE DATABASE IF NOT EXISTS dividdi;
USE dividdi;

-- Tabella UTENTE
CREATE TABLE UTENTE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Tabella DVD
CREATE TABLE DVD (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    durata INT NOT NULL,
    regista VARCHAR(100) NOT NULL
);

-- Tabella PREZZO
CREATE TABLE PREZZO (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dvd_id INT NOT NULL,
    valore DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (dvd_id) REFERENCES DVD(id) ON DELETE CASCADE
);

-- Tabella ORDINE
CREATE TABLE ORDINE (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utente_id INT NOT NULL,
    dvd_id INT NOT NULL,
    prezzo_tot DECIMAL(10, 2) NOT NULL,
    data_ordine TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utente_id) REFERENCES UTENTE(id) ON DELETE CASCADE,
    FOREIGN KEY (dvd_id) REFERENCES DVD(id) ON DELETE CASCADE
);

-- Creazione indici per migliorare le query
CREATE INDEX idx_ordine_utente ON ORDINE(utente_id);
CREATE INDEX idx_ordine_dvd ON ORDINE(dvd_id);
CREATE INDEX idx_prezzo_dvd ON PREZZO(dvd_id);
