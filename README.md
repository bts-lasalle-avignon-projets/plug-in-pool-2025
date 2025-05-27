<table>
    <tr>
        <th colspan="3">Plug-In-Pool</th>
    </tr>
    <tr>
        <td>
        <a href="https://fr.wikipedia.org/wiki/Android"><img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/></a>
        </td>
        <td>
        <a href="https://fr.wikipedia.org/wiki/Qt"><img src="https://img.shields.io/badge/Qt-%23217346.svg?style=for-the-badge&logo=Qt&logoColor=white" alt="Qt"/></a>
        </td>
        <td>
        <a href="https://github.com/bts-lasalle-avignon-projets/plug-in-pool-2025"><img src="https://img.shields.io/badge/Projet-BTS%20CIEL-darkviolet.svg" alt="Projet BTS CIEL"/></a>
        </td>
    </tr>
</table>

<table>
    <tr>
        <th colspan="2">Plug-In-Pool</th>
    </tr>
    <tr>
        <td>
        <a href="https://github.com/bts-lasalle-avignon-projets/plug-in-pool-2025/actions/workflows/qt-build.yml"><img src="https://github.com/bts-lasalle-avignon-projets/plug-in-pool-2025/actions/workflows/qt-build.yml/badge.svg" alt="Android"/></a>
        </td>
        <td>
        <a href="https://github.com/bts-lasalle-avignon-projets/plug-in-pool-2025/actions/workflows/android-build.yml"><img src="https://github.com/bts-lasalle-avignon-projets/plug-in-pool-2025/actions/workflows/android-build.yml/badge.svg" alt="Qt"/></a>
        </td>
    </tr>
</table>

# Projet 2025 : Plug-In-Pool

- [Projet 2025 : Plug-In-Pool](#projet-2025--plug-in-pool)
  - [Présentation](#présentation)
  - [Déroulement d'un Match](#déroulement-dun-match)
  - [Modules IR](#modules-ir)
    - [Application Android](#application-android)
      - [Module de gestion de matchs](#module-de-gestion-de-matchs)
      - [Maquette de l'application Android](#maquette-de-lapplication-android)
      - [Recette](#recette)
      - [Diagramme de cas d'utilisation Android](#diagramme-de-cas-dutilisation-android)
      - [Base de données](#base-de-données)
    - [Application Qt](#application-qt)
      - [Module de visualisation de matchs](#module-de-visualisation-de-matchs)
      - [Maquette de l'application Qt](#maquette-de-lapplication-qt)
      - [Recette](#recette-1)
  - [Itérations](#itérations)
    - [Itération 1](#itération-1)
    - [Itération 2](#itération-2)
    - [Itération 3](#itération-3)
    - [Itération 4](#itération-4)
  - [Changelog](#changelog)
  - [TODO](#todo)
  - [Défauts constatés non corrigés](#défauts-constatés-non-corrigés)
  - [Équipe de développement](#équipe-de-développement)

---

## Présentation

Le système **Plug in Pool** est un système numérique permettant de jouer un match de _blackball_ (parfois appelé billard anglais, billard pool ou 8 pool).

Les matchs sont réalisées en $n$ parties gagnantes.

Le _blackball_ est un jeu de billard qui se déroule sur une table rectangulaire à 6 poches avec 14 billes de couleur (7 rouges et 7 jaunes), une bille noire portant le numéro 8 et une bille d'impact blanche. Les joueurs jouent uniquement les billes de leur groupe : les jaunes ou les rouges. Si un joueur empoche une de ses billes, il est autorisé à rejouer. La partie est gagnée par le joueur (ou l’équipe) qui, après avoir empoché les 7 billes de sa couleur, empoche la bille noire numéro 8.

> voir [le déroulement d'une partie](#déroulement-dune-partie)

![](./images/tableDeBillard.png)

Chaque table de billard est équipée de capteurs permettant de détecter dans quelle poche une bille a été empochée ainsi que sa couleur.

Ensuite, afin que ces données recueillies par les capteurs soient transmises à un appareil Android pour la gestion de la partie, un module Bluetooth sera utilisé pour assurer la transmission des informations entre la table et la tablette, dans les deux sens.

![](./images/structureDuSysteme.png)

---

## Déroulement d'un Match


## Modules IR

### Application Android

#### Module de gestion de matchs

Sur le terminal mobile Android, l'application permet de paramétrer un match et démarrer une ou plusieurs partie(s).

Ainsi, les joueurs peuvent :

- Saisir leur nom
- Paramétrer le match entre deux joueurs
  - Saisir le nombre de parties gagnantes
- Connecter le terminal mobile Android à une table
- Lancer le match
- Gérer et visualiser le déroulement de la partie
  - Changer automatiquement de joueur
  - Afficher le nombre de points

Le terminal mobile Android, stocke chaque match dans une base de données SQLite, où il est possible pour l'utilisateur en se rendant dans l'historique, de la visualiser ou la purger. Le terminal Android permet de se connecter avec à une table par liaison Bluetooth pour communiquer avec elle, mais aussi pour assurer une liaison avec l'écran d'affichage.

#### IHM de l'application Android

![](./images/accueilEtCreationOuChoixDesJoueurs.png)

![](./images/choixDesAppreilsEtActivitePartie.png)

![](./images/demarerPartieEtEmpochage.png)

![](./images/finDePartieEtHistorique.png)

#### Recette

<table>
  <tr>
    <td style="font-weight: bold;">Fonctionalités</td>
    <td style="font-weight: bold;">A faire</td>
    <td style="font-weight: bold;">En cours</td>
    <td style="font-weight: bold;">Terminé</td>
  </tr>
  <tr>
    <td>Créer un joueur</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Paraméter un match</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Lancer un match</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Gérer le déroulement d'un match</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Enregistrer les données des parties</td>
    <td style="text-align: center;">x</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
  </tr>
  <tr>
    <td>Consulter l'historique des matchs</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Purger l'historique</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Dialoguer avec l'écran</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
    <tr>
    <td>Dialoguer avec la table de billard</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
</table>

#### Diagramme de cas d'utilisation Android

![](./images/casUtilisationAndroid.png)

#### Base de données

![](./images/bdd.jpg)

```sql
CREATE TABLE IF NOT EXISTS "joueurs" (
    "idJoueur"  INTEGER,
    "nom"       VARCHAR(64),
    "prenom"    VARCHAR(64),
    "points"    INTEGER DEFAULT 0,
    PRIMARY KEY("idJoueur" AUTOINCREMENT),
    UNIQUE("nom","prenom")
);

CREATE TABLE IF NOT EXISTS "matchs" (
    "idMatch"               INTEGER,
    "nom"                   VARCHAR(64),
    "idJoueur1"             INTEGER NOT NULL,
    "idJoueur2"             INTEGER NOT NULL,
    "nbPartiesGagnantes"    INTEGER DEFAULT 1,
    "fini"                  INTEGER DEFAULT 0,
    "horodatage"            DATETIME NOT NULL,
    PRIMARY KEY("idMatch" AUTOINCREMENT),
    FOREIGN KEY("idJoueur1") REFERENCES "joueurs"("idJoueur") ON DELETE CASCADE,
    FOREIGN KEY("idJoueur2") REFERENCES "joueurs"("idJoueur") ON DELETE CASCADE
);

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
```

### Application Qt

#### Module de visualisation de matchs

Ce module correspond à la partie “affichage” du système. Il a pour objectifs de réaliser la récupération d’informations envoyées par le terminal mobile et l’affichage de la rencontre actuelle. Il communique en Bluetooth uniquement avec le terminal mobile Android.

Sur l'écran, les joueurs pourront visualiser en continu :

- Le nom des joueurs (si existant), la durée écoulée du match
- Les billes empochées et restantes
- Le nombre de manches gagnées par chaque joueur
- Des statistiques

#### Maquette de l'application Qt

![](./images/AccueilEcran.png)

![](./images/RencontreEcran.png)

![](./images/FinEcran.png)

#### Recette

<table>
  <tr>
    <td style="font-weight: bold;">Fonctionalités</td>
    <td style="font-weight: bold;">A faire</td>
    <td style="font-weight: bold;">En cours</td>
    <td style="font-weight: bold;">Terminé</td>
  </tr>
  <tr>
    <td>Afficher un écran d'accueil</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Visualiser l’empochage d’une bille</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Visualiser les données de la partie en temps réel</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
  <tr>
    <td>Visualiser les données du match</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
    <td style="text-align: center;"></td>
  </tr>
  <tr>
    <td>Dialoguer avec le terminal mobile</td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;"></td>
    <td style="text-align: center;">x</td>
  </tr>
</table>

## Itérations

### Itération 1

> Du 29 Janvier 2025 au 28 Mars 2025

- [x] Créer les maquettes des interfaces
- [x] Initialiser le dépot
- [x] Créer les vues Android

### Itération 2

> Du 29 Mars 2025 au 23 Mai 2025

- [x] Paramétrer un match
- [x] Lancer une partie
- [x] Choisir un ou des joueurs existants pour lancer un match
- [x] Créer un ou des nouveaux joueurs
- [x] Enregister dans la base de données les nouveaux joueurs
- [x] Gérer la casse et une manche
- [x] Liaison entre la table de billard et le terminal mobile Android
- [x] Liaison entre le terminal mobile Android et l'ecran d'affichage

### Itération 3

> Du 24 Mai 2025 au 30 Mai 2025

- [x] Jouer une parties
- [x] Afficher les données de la partie en cours
- [x] Afficher le joueur gagant
- [x] Visualiser l'historique
- [x] Purger l'historique

### Itération 4

> Du 31 Mai 2025 au 15 Juin 2025

- [ ] Jouer plusieurs parties
- [ ] Enregistrer les données du match dans la base de données

## Changelog

### Verson 1.0

- [x] Jouer une parties
- [x] Afficher les données de la partie en cours
- [x] Afficher le joueur gagant
- [x] Visualiser l'historique
- [x] Purger l'historique

### Version 0.1

- [x] Créer les maquettes des interfaces
- [x] Initialiser le dépot
- [x] Créer les vues Android
- [x] Paramétrer un match
- [x] Lancer une ou plusieurs partie(s)
- [x] Choisir un ou des joueurs existants pour lancer un match
- [x] Créer un ou des nouveaux joueurs
- [x] Enregister dans la base de données les nouveaux joueurs
- [x] Gérer la casse et une manche
- [x] Liaison entre la table de billard et le terminal mobile Android
- [x] Liaison entre le terminal mobile Android et l'ecran d'affichage

## TODO

## Défauts constatés non corrigés

### Android

- Si la première tentative de connexion à un appareils Bluetooth échoue. Il apparait comme : *"Erreur de connexion"* dans l'application Android, alors qu'il est en réalité bien connecté.

### Qt

- Si deux trames sont envoyées à la suite depuis la tablette Android vers l'écran de visualisation. Elles sont considérées comme une seule.

## Équipe de développement

- MILLOT Pierre : [**[pierre(dot)millot(dot)pro(at)gmail(dot)com](mailto:pierre.millot.pro@gmail.com)**]
- NAVARRO Mattéo : [**[matteo(dot)navarro(dot)pro(at)gmail(dot)com](mailto:matteo.navarro.pro@gmail.com)**]
- VIVANCOS Evan : [**[evan(dot)vivancos(dot)pro(at)gmail(dot)com](mailto:evan.vivancos.pro@gmail.com)**]

---
&copy; 2024-2025 BTS LaSalle Avignon
