# Simulateur PLUG-IN-POOL 2025

## Présentation du protocole implanté dans le simulateur ESP'ACE

Ce document présente rapidement le fonctionnement du simulateur ainsi que le protocole implémenté. Le protocole complet est disponible dans Google Drive. Actuellement, la version du protocole est la **1.0**.

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

Un tir prend entre 0.5 et 4 secondes.

On peut définir la précision d'empochage :

```cpp
#define PRECISION_TIR  80   //!< Précision d'empochage (en %)
```

Ici, le joueur simulé a 80 % de chance d'empocher une bille sinon il loupe. Une trame est envoyée à la tablette seulement s'il a empoché une bille.

## Protocole Table - Mobile

Format général :

- Contenu : caractères ASCII
- Entête : `$`
- Séparateur : `/`
- Délimiteur de fin : `!`

Le protocole distingue deux types de trame :

- trame de service émise par la tablette
- trame de données (empochage) émise par la table

### Trame A (Activation de la détection) : Mobile → Table

Format : `$A!`

> Le simulateur "refait" le plein de billes JAUNE et ROUGE. Les billes BLANCHE et NOIRE sont toujours sur la table.

### Trame D (Désactivation de la détection) : Mobile → Table

Format : `$D!`

> Désactive les boutons SW1 et SW2 (qui simule la détection d'empochage)

### Trame E (Empochage) : Table → Mobile

Format : `$E/couleurBille/idPoche!`

- `couleurBille` : la couleur de bille

|    Couleur    | Valeur |
| :-----------: | :----: |
|  Bille Rouge  |   0    |
|  Bille Jaune  |   1    |
| Bille Blanche |   2    |
|  Bille Noire  |   3    |

- `idPoche` : l'identifiant de la poche

|        Poche         | Valeur |
| :------------------: | :----: |
|   Poche Nord-Ouest   |   1    |
|    Poche Nord-Est    |   2    |
| Poche Équateur-Ouest |   3    |
|  Poche Équateur-Est  |   4    |
|   Poche Sud-Ouest    |   5    |
|    Poche Sud-Est     |   6    |

![](./images/table.png)

### Fonctionnement

Pour l'instant, le simulateur démarre la détection de bille à la réception d'une trame : `$A!`

Utilisation :

- Le bouton SW1 permet de simuler un tir
- le bouton SW2 permet de simuler une séquence victorieuse de tir (toutes les billes restantes d'une couleur suivi d'un empochage de la bille NOIRE).

## Écran OLED

Le nom du périphérique bluetooth et son adresse MAC sont affichés sur les deux premières lignes.

Les lignes suivantes affichent successivement :

- l'état de la détection
- le résultat d'un tir
- le nombre de billes rouges restantes
- le nombre de billes jaunes restantes

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
