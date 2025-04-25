--- LDD (langage de définition de données)
--- plug-in-pool.db

--- Supprime les tables

DROP TABLE IF EXISTS "manches";
DROP TABLE IF EXISTS "matchs";
DROP TABLE IF EXISTS "joueurs";

--- Table joueurs

CREATE TABLE IF NOT EXISTS "joueurs" (
    "idJoueur"  INTEGER,
    "nom"       VARCHAR(64),
    "prenom"    VARCHAR(64),
    "points"    INTEGER DEFAULT 0,
    PRIMARY KEY("idJoueur" AUTOINCREMENT),
    UNIQUE("nom","prenom")
);

--- Table matchs

CREATE TABLE IF NOT EXISTS "matchs" (
    "idMatch"               INTEGER,
    "nom"                   VARCHAR(64),
    "idJoueur1"             INTEGER NOT NULL,
    "idJoueur2"             INTEGER NOT NULL,
    "nbManchesGagnantes"    INTEGER DEFAULT 5,
    "fini"                  INTEGER DEFAULT 0,
    "horodatage"            DATETIME NOT NULL,
    PRIMARY KEY("idMatch" AUTOINCREMENT),
    FOREIGN KEY("idJoueur1") REFERENCES "joueurs"("idJoueur") ON DELETE CASCADE,
    FOREIGN KEY("idJoueur2") REFERENCES "joueurs"("idJoueur") ON DELETE CASCADE
);

--- Table manches

CREATE TABLE IF NOT EXISTS "manches" (
    "idManche"      INTEGER,
    "idMatch"       INTEGER NOT NULL,
    "idGagnant"     INTEGER,
    "idPerdant"     INTEGER,
    "numeroTable"   INTEGER NOT NULL,
    "horodatage"    DATETIME NOT NULL UNIQUE,
    PRIMARY KEY("idManche" AUTOINCREMENT),
    FOREIGN KEY("idGagnant") REFERENCES "joueurs"("idJoueur") ON DELETE CASCADE,
    FOREIGN KEY("idPerdant") REFERENCES "joueurs"("idJoueur") ON DELETE CASCADE,
    FOREIGN KEY("idMatch") REFERENCES "matchs"("idMatch") ON DELETE CASCADE
);
