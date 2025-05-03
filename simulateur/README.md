# Simulateur PLUG-IN-POOL 2025

## Présentation du protocole implanté dans le simulateur ESP'ACE

Ce document présente rapidement le fonctionnement du simulateur ainsi que le protocole implémenté. Le protocole complet est disponible dans Google Drive. Actuellement, la version du protocole est la **0.3**.

Le système PLUG-IN-POOL est composé de trois modules :

- Module de gestion de matchs (Tablette Android)
- Module de visualisation de matchs (Raspberry Pi / Qt / Écran de télévision)
- Module de détection des billes (ESP32 / Table de billard)

Ce document présente la communication entre le module de gestion de matchs (Tablette Android) et le module de détection des billes (ESP32 / Table de billard).

> Communication **Bluetooth** classic : on met en oeuvre RFCOMM (*Radio frequency communication*) basé sur les spécifications RS-232 et qui émule des liaisons séries. On utilise alors un profil SPP (*Serial Port Profile*). Les messages echangés sont orientés caractère (ASCII).

La table de billard est équipée d'un détecteur d'empochage de billes qui permet de déterminer le numéro de poche et la couleur de la bille empochée. Le système de détection n'est actif que pendant une partie. La table ne gère pas la partie et ne détecte pas de faute ni évidemment de loupé. Elle affiche les évènements sur un écran (ici OLED pour le simulateur).

## Configuration du simulateur

Il faut définir le numéro de table :

```cpp
#define NUMERO_TABLE 2 //!< Numéro de la table
```

> Cela génère un nom de périphérique Bluetooth `pool-x` où `x` est le `NUMERO_TABLE`

Valeurs par défaut :

```cpp
#define NB_POCHES       6
#define NB_BILLES_ROUGE 7
#define NB_BILLES_JAUNE 7
```

Un tir prend entre 1 et 4 secondes. Le joueur a 1 chance sur 2 d'empocher une bille sinon il loupe ou il fait une faute (empochage d'une mauvaise bille). Une trame est envoyée à la tablette seulement s'il a empoché une bille.

## Protocole Table - Mobile

Format général :

- Contenu : caractères ASCII
- Entête : `$`
- Séparateur : `/`
- Délimiteur de fin : `!`

Le protocole distingue deux types de trame :

- trame de service émise par la tablette
- trame de données (empochage) émise par la table

### Trame M (Manche) : Mobile → Table

Format : `$M/nn/Jx/Ei/j!`

Les champs :

- `nn` : le numéro de table (`01`, `02`, ...)
- `x` : le numéro de joueur `1` ou `2` (`0` pour joueur non défini)
- `i` : l'état de la manche

| État de la manche | Valeur |
| :---------------: | :----: |
|  Début de manche  |   1    |
|   Fin de manche   |   0    |

- `j` : changement de tour

|     Jeu      | Valeur |
| :----------: | :----: |
|    Casse     |   C    |
|    Faute     |   F    |
| Point validé |   V    |
|  Non défini  |   N    |

### Trame E (Empochage) : Table → Mobile

Format : `$E/nn/Jx/Pi/Bj!`

Les champs :

- `nn` : le numéro de table (`01`, `02`, ...)
- `x` : le numéro de joueur `1` ou `2`
- `i` : le numéro de poche

|        Poche         | Valeur |
| :------------------: | :----: |
|   Poche Nord-Ouest   |   P1   |
|    Poche Nord-Est    |   P2   |
| Poche Équateur-Ouest |   P3   |
|  Poche Équateur-Est  |   P4   |
|   Poche Sud-Ouest    |   P5   |
|    Poche Sud-Est     |   P6   |

![](./images/table.png)

- `j` : la couleur de bille

|    Couleur    | Valeur |
| :-----------: | :----: |
|  Bille Rouge  |   BR   |
|  Bille Jaune  |   BJ   |
| Bille Blanche |   BB   |
|  Bille Noire  |   BN   |

### Fonctionnement

Pour l'instant, le simulateur démarre à la réception d'une trame : `$M/02/J0/E1/N!` (pour la table n°2).

Quand le simulateur affiche :

- "loupé"   : il faut appuyer sur le bouton TOUR (SW2).
- "faute !" : il faut appuyer sur le bouton FAUTE (SW1).

La partie peut être arrêtée avec une trame ``$M/02/J0/E0/N!`` (pour la table n°2).

## Écran OLED

Le nom du périphérique bluetooth et son adresse MAC sont affichés sur les deux premières lignes.

Les lignes suivantes affichent successivement :

- le score du joueur en train de tirer
- le tir du joueur qui a les billes rouges
- le tir du joueur qui a les billes jaunes
- la trame reçue et l'état de la partie

*Remarque :* `>` indique le joueur en train de tirer.

## platform.ini

```ini
[env:lolin32]
platform = espressif32
board = lolin32
framework = arduino
lib_deps =
  thingpulse/ESP8266 and ESP32 OLED driver for SSD1306 displays @ ^4.2.0
upload_port = /dev/ttyUSB0
upload_speed = 115200
monitor_port = /dev/ttyUSB0
monitor_speed = 115200
```

## Auteur

- Thierry Vaira <<tvaira@free.fr>>
