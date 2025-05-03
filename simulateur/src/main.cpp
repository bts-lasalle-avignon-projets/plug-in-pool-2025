/**
 * @file src/main.cpp
 * @brief Programme principal PLUG-IN-POOL 2025
 * @author Thierry Vaira
 * @version 0.1
 */
#include <Arduino.h>
#include <BluetoothSerial.h>
#include <afficheur.h>
#include "esp_bt_main.h"
#include "esp_bt_device.h"

#define NUMERO_TABLE 2 //!< Numéro de la table

#define DEBUG
// #define DEBUG_VERIFICATION

// Brochages
#define GPIO_LED_ROUGE   5    //!< En attente
#define GPIO_LED_ORANGE  17   //!< (non utilisé)
#define GPIO_LED_VERTE   16   //!< Trame START
#define GPIO_SW1         12   //!< Pour simuler un tir
#define GPIO_SW2         14   //!< (non utilisé)
#define ADRESSE_I2C_OLED 0x3c //!< Adresse I2C de l'OLED
#define BROCHE_I2C_SDA   21   //!< Broche SDA
#define BROCHE_I2C_SCL   22   //!< Broche SCL

// Protocole (cf. Google Drive)
#define EN_TETE_TRAME       "$"
#define DELIMITEUR_CHAMP    "/"
#define DELIMITEURS_FIN     "!\n"
#define DELIMITEUR_DATAS    '/'
#define DELIMITEUR_FIN      '\n'
#define INDEX_TYPE_TRAME    1
#define NUMERO_CHAMP_TYPE   0
#define NUMERO_CHAMP_TABLE  1
#define NUMERO_CHAMP_JOUEUR 2
#define NUMERO_CHAMP_MANCHE 3
#define NUMERO_CHAMP_ETAT   4

// Mobile-pool -> Table - Format : $M/nn/Jx/Ei/j! - Exemple : $M/02/J1/E1/C!
#define LONGUEUR_TRAME        14
#define TRAME_MANCHE          'M'
#define CHAMP_JOUEUR          'J'
#define CHAMP_ETAT            'E'
#define CHAMP_START           '1' // Commencer une manche (réinitialisation)
#define CHAMP_STOP            '0' // Arrêter une manche (facultatif)
#define CHAMP_ETAT_CASSE      'C'
#define CHAMP_ETAT_FAUTE      'F'
#define CHAMP_ETAT_VALIDE     'V'
#define CHAMP_ETAT_NON_DEFINI 'N'
// Mobile-pool <- Table - Format : $E/nn/Jx/Pi/Bj!
#define TRAME_EMPOCHE 'E'
#define CHAMP_POCHE   'P'
#define CHAMP_BILLE   'B'

#define ERREUR_TRAME_INCONNUE      0
#define ERREUR_TRAME_NON_SUPPORTEE 1
#define ERREUR_TRAME_ETAT          2

// Blackpool
#define NB_POCHES         6
#define NB_BILLES         7
#define NB_BILLES_ROUGE   7
#define NB_BILLES_JAUNE   7
#define NB_BILLES_BLANCHE 1
#define NB_BILLES_NOIRE   1

// Divers
#define ANTI_REBOND 400 //

#define BLUETOOTH
#ifdef BLUETOOTH
#define BLUETOOTH_SLAVE //!< esclave
// #define BLUETOOTH_MASTER //!< maître
BluetoothSerial ESPBluetooth;
#endif

/**
 * @enum EtatPartie
 * @brief Les differents états d'une partie
 */
enum EtatPartie
{
    Arretee = 0,
    EnCours
};

/**
 * @enum CouleurBille
 * @brief Les couleurs des billes
 */
enum CouleurBille
{
    AUCUNE  = -1,
    ROUGE   = 0, // Bille de but
    JAUNE   = 1, // Bille de but
    BLANCHE = 2, // Bille de choc
    NOIRE   = 3, // Bille de but
    NbCouleurs
};

/**
 * @enum Poche
 * @brief Les numéros de poche
 */
enum Poche
{
    A = 1,
    B,
    C,
    D,
    E,
    F /* = 6 */
};

// Variables globales
EtatPartie   etatPartie   = Arretee; //!< l'état de la partie
int          numeroJoueur = 0;       //!< le numéro du joueur
CouleurBille couleurBilleEmpochee =
  CouleurBille::AUCUNE; //!< la couleur de la bille empochée
int nbBilles[CouleurBille::NbCouleurs] = {
    NB_BILLES_ROUGE,
    NB_BILLES_JAUNE,
    NB_BILLES_BLANCHE,
    NB_BILLES_NOIRE
}; //!< le nombre de billes par couleur
const String codeCouleur[CouleurBille::NbCouleurs] = {
    "ROUGE",
    "JAUNE",
    "BLANCHE",
    "NOIRE"
}; //!< code de couleur de la bille empochée (affichage)
bool      refresh    = false; //!< demande rafraichissement de l'écran OLED
bool      antiRebond = false; //!< anti-rebond
bool      tirEncours = false; //!< une sequence de tirs est en cours
Afficheur afficheur(ADRESSE_I2C_OLED,
                    BROCHE_I2C_SDA,
                    BROCHE_I2C_SCL); //!< afficheur OLED SSD1306
// Protocole
int          numeroTable   = NUMERO_TABLE;
String       entete        = String(EN_TETE_TRAME);    // caractère séparateur
String       separateur    = String(DELIMITEUR_CHAMP); // caractère séparateur
String       delimiteurFin = String(DELIMITEURS_FIN);  // fin de trame
const String codeCouleurProtocole[CouleurBille::NbCouleurs] = {
    "R",
    "J",
    "B",
    "N"
}; //!< code de couleur de la bille empochée (protocole)
const String codePoche[NB_POCHES] = {
    "A", "B", "C", "D", "E", "F"
}; //!< code des poches (protocole)
const String codeEtat[] = { "casse",
                            "valide",
                            "faute" }; //!< code des messages d'état du tour

String extraireChamp(String& trame, unsigned int numeroChamp)
{
    String       champ;
    unsigned int compteurCaractere  = 0;
    unsigned int compteurDelimiteur = 0;
    char         fin                = '\n';

    if(delimiteurFin.length() > 0)
        fin = delimiteurFin[0];

    for(unsigned int i = 0; i < trame.length(); i++)
    {
        if(trame[i] == separateur[0] || trame[i] == fin)
        {
            compteurDelimiteur++;
        }
        else if(compteurDelimiteur == numeroChamp)
        {
            champ += trame[i];
            compteurCaractere++;
        }
    }

    return champ;
}

/**
 * @brief Envoie une trame d'empochage via le Bluetooth
 *
 */
void envoyerTrameEmpoche(Poche numeroPoche, CouleurBille couleurBille)
{
    char trameEnvoi[64];

    // Format : $E/nn/Jn/PPi/Bj!
    sprintf((char*)trameEnvoi,
            "%sE/%02d/J%d/P%d/B%s!",
            entete.c_str(),
            numeroTable,
            numeroJoueur,
            int(numeroPoche),
            codeCouleurProtocole[int(couleurBille)].c_str());
    ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char*)trameEnvoi));
    couleurBilleEmpochee = couleurBille;
#ifdef DEBUG
    String trame = String(trameEnvoi);
    // trame.remove(trame.indexOf("\r"), 1);
    Serial.print("> ");
    Serial.println(trame);
#endif
}

/**
 * @brief Lit une trame via le Bluetooth
 *
 * @fn lireTrame(String &trame)
 * @param trame la trame reçue
 * @return bool true si une trame a été lue, sinon false
 */
bool lireTrame(String& trame)
{
    if(ESPBluetooth.available())
    {
        trame.clear();
        trame = ESPBluetooth.readStringUntil(DELIMITEUR_FIN);
#ifdef DEBUG
        Serial.print("< ");
        Serial.println(trame);
        // Serial.print("longueur : ");
        // Serial.println(trame.length());
#endif
        trame.concat(DELIMITEUR_FIN); // remet le DELIMITEUR_FIN
        return true;
    }

    return false;
}

/**
 * @brief Vérifie si la trame reçue est valide et retorune le type de la trame
 *
 * @fn verifierTrame(uint8_t &trame)
 * @param trame
 * @return bool
 */
bool verifierTrame(String& trame)
{
    // longueur de la trame ?
    if(trame.length() != LONGUEUR_TRAME + 1) // avec le \n
    {
#ifdef DEBUG
        Serial.println("Erreur : longueur invalide !");
#endif
        return false;
    }
    // en-tête de la trame ?
    if(!trame.startsWith(EN_TETE_TRAME))
    {
#ifdef DEBUG
        Serial.println("Erreur : entete absente ou invalide !");
#endif
        return false;
    }
    // séparateur de champ ?
    if(trame.indexOf(DELIMITEUR_CHAMP) == -1)
    {
#ifdef DEBUG
        Serial.println("Erreur : delimiteur de champ absent !");
#endif
        return false;
    }
    // séparateur de fin ?
    if(!trame.endsWith(DELIMITEURS_FIN))
    {
#ifdef DEBUG
        Serial.println("Erreur : delimiteur de fin absent ou invalide !");
#endif
        return false;
    }
    return true;
}

/**
 * @brief Calcule le nombre de billes restantes
 *
 */
void calculerScore()
{
    if(couleurBilleEmpochee == CouleurBille::ROUGE ||
       couleurBilleEmpochee == CouleurBille::JAUNE)
    {
        if(nbBilles[couleurBilleEmpochee] > 0)
        {
            // on retire la bille empochée
            nbBilles[couleurBilleEmpochee]--;
        }
    }
}

/**
 * @brief Retourne le nombre de billes restantes et empochées
 *
 */
String getScore(CouleurBille couleurJoueur)
{
    String scoreJoueur;
    for(int i = 0; i < nbBilles[couleurJoueur]; ++i)
    {
        scoreJoueur += String("O");
    }
    for(int i = nbBilles[couleurJoueur]; i < NB_BILLES; ++i)
    {
        scoreJoueur += String("X");
    }
    return scoreJoueur;
}

/**
 * @brief Réinitialise l'affichage OLED des lignes
 *
 */
void reinitialiserAffichage()
{
    afficheur.setMessageLigne(Afficheur::Ligne1, "");
    afficheur.setMessageLigne(Afficheur::Ligne2, "");
    afficheur.setMessageLigne(Afficheur::Ligne3, "");
    afficheur.setMessageLigne(Afficheur::Ligne4, "");
    refresh = true;
}

/**
 * @brief Affiche le score
 *
 */
void afficherScore()
{
    char   strMessageDisplay[24];
    String score;

    score = getScore(CouleurBille::ROUGE);
    sprintf(strMessageDisplay, " Rouge : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
    score = getScore(CouleurBille::JAUNE);
    sprintf(strMessageDisplay, " Jaune : %s", score.c_str());
    afficheur.setMessageLigne(Afficheur::Ligne4, String(strMessageDisplay));

    // afficheur.afficher();
}

/**
 * @brief Affiche le résultat d'un tir
 *
 */
void afficherTir(int numeroPoche, CouleurBille couleurBille)
{
    char strMessageDisplay[24];

    // empochage ?
    if(numeroPoche >= 1 && numeroPoche <= NB_POCHES)
    {
        sprintf(strMessageDisplay,
                "Tir poche %d : %s",
                numeroPoche,
                codeCouleur[int(couleurBille)].c_str());
    }
    else // loupé : non détectable
    {
        sprintf(strMessageDisplay, "Tir loupé");
    }
    afficheur.setMessageLigne(Afficheur::Ligne2, String(strMessageDisplay));
    afficheur.afficher();
}

/**
 * @brief Simule un tir et la détection d'empochage
 *
 */
CouleurBille simulerCouleurBille()
{
    bool         couleurValide = false;
    CouleurBille couleurBille  = CouleurBille::AUCUNE;

    do
    {
        couleurBille = (CouleurBille)random((long)CouleurBille::ROUGE,
                                            (long)(CouleurBille::NbCouleurs));
        if(nbBilles[couleurBille] > 0)
            couleurValide = true;
    } while(!couleurValide);

    return couleurBille;
}

/**
 * @brief Simule un tir et la détection d'empochage
 *
 */
bool simulerTir()
{
    if(etatPartie != EnCours)
        return false;
#ifdef DEBUG
    Serial.print("simulerTir()");
#endif
    int poche =
      random(0, long(NB_POCHES * 2)) + 1; // 1 chance sur 2 : entre 1 et 12
    CouleurBille couleurBille = simulerCouleurBille();
    // empochage ?
    if(poche >= 1 && poche <= NB_POCHES)
    {
#ifdef DEBUG
        Serial.print(" -> empochage poche ");
        Serial.print(poche);
        Serial.print(" couleur ");
        Serial.println(codeCouleur[int(couleurBille)]);
#endif
        envoyerTrameEmpoche(Poche(poche), couleurBille);
        afficherTir(poche, couleurBille);
        return true;
    }
    else // loupé : non détectable
    {
#ifdef DEBUG
        Serial.println("  -> loupé");
#endif
        afficherTir(poche, couleurBille);
        return false;
    }
}

/**
 * @brief Tirer
 * @fn tirer()
 */
void tirer()
{
    if(tirEncours)
    {
        afficheur.setMessageLigne(Afficheur::Ligne2, String(""));
        afficheur.afficher();
        delay(random(1000, 4000)); // temps pour joueur
        simulerTir();
        tirEncours = false;
    }
}

/**
 * @brief Déclencher un tir interruption sur le bouton SW1
 * (tour suivant)
 * @fn declencherTir()
 */
void IRAM_ATTR declencherTir()
{
    if(etatPartie != EnCours || antiRebond)
        return;

    tirEncours = true;
    antiRebond = true;
}

/**
 * @brief Initialise les ressources du programme
 *
 * @fn setup
 *
 */
void setup()
{
    Serial.begin(115200);
    while(!Serial)
        ;

    pinMode(GPIO_LED_ROUGE, OUTPUT);
    pinMode(GPIO_LED_ORANGE, OUTPUT);
    pinMode(GPIO_LED_VERTE, OUTPUT);

    pinMode(GPIO_SW1, INPUT_PULLUP);
    attachInterrupt(digitalPinToInterrupt(GPIO_SW1), declencherTir, FALLING);

    digitalWrite(GPIO_LED_ROUGE, HIGH);
    digitalWrite(GPIO_LED_ORANGE, LOW);
    digitalWrite(GPIO_LED_VERTE, LOW);

    afficheur.initialiser();

    String titre  = "";
    String stitre = "=====================";

#ifdef BLUETOOTH
#ifdef BLUETOOTH_MASTER
    String nomBluetooth = "iot-esp-maitre";
    ESPBluetooth.begin(nomBluetooth, true);
    const uint8_t* adresseESP32 = esp_bt_dev_get_address();
    char           str[18];
    sprintf(str,
            "%02x:%02x:%02x:%02x:%02x:%02x",
            adresseESP32[0],
            adresseESP32[1],
            adresseESP32[2],
            adresseESP32[3],
            adresseESP32[4],
            adresseESP32[5]);
    stitre = String("== ") + String(str) + String(" ==");
    titre  = nomBluetooth;
#else
    String nomBluetooth = "pool-" + String(numeroTable);
    ESPBluetooth.begin(nomBluetooth);
    const uint8_t* adresseESP32 = esp_bt_dev_get_address();
    char           str[18];
    sprintf(str,
            "%02x:%02x:%02x:%02x:%02x:%02x",
            adresseESP32[0],
            adresseESP32[1],
            adresseESP32[2],
            adresseESP32[3],
            adresseESP32[4],
            adresseESP32[5]);
    stitre = String("=== ") + String(str) + String(" ===");
    titre  = String("Bluetooth : ") + nomBluetooth + String("     2023");
#endif
#endif

#ifdef DEBUG
    Serial.println(titre);
    Serial.println(stitre);
#endif

    afficheur.setTitre(titre);
    afficheur.setSTitre(stitre);
    afficheur.setMessageLigne(Afficheur::Ligne1, String("Attente ..."));
    afficheur.afficher();

    // initialise le générateur pseudo-aléatoire
    // Serial.println(randomSeed(analogRead(34)));
    Serial.println(esp_random());
}

/**
 * @brief Boucle infinie d'exécution du programme
 *
 * @fn loop
 *
 */
void loop()
{
    String trame;

    if(refresh)
    {
        afficheur.afficher();
        refresh = false;
    }

    if(antiRebond)
    {
        afficheur.afficher();
        delay(ANTI_REBOND);
        antiRebond = false;
    }

    if(etatPartie == EnCours && tirEncours)
    {
        tirer();
    }

    if(lireTrame(trame))
    {
        if(verifierTrame(trame))
        {
            refresh = true;

            if(trame.charAt(INDEX_TYPE_TRAME) == TRAME_MANCHE)
            {
                // Mobile-pool -> Table : M/nn/Jx/Ei/j!
                String numeroTableBillard =
                  extraireChamp(trame, NUMERO_CHAMP_TABLE);
                String champNumeroJoueur =
                  extraireChamp(trame, NUMERO_CHAMP_JOUEUR);
                String etatManche = extraireChamp(trame, NUMERO_CHAMP_MANCHE);
                String etatTour   = extraireChamp(trame, NUMERO_CHAMP_ETAT);
#ifdef DEBUG
                Serial.println("Trame : " + trame);
                Serial.println("numeroTableBillard : " + numeroTableBillard);
                Serial.println("numeroJoueur : " + champNumeroJoueur);
                Serial.println("etatManche : " + etatManche);
                Serial.println("etatTour : " + etatTour);
#endif
                if(etatManche.charAt(0) == CHAMP_ETAT)
                {
                    int etat = etatManche.charAt(1) - '0';
                    if(etat == 1)
                    {
                        numeroJoueur = champNumeroJoueur.charAt(1) - '0';
                        if(etatPartie == Arretee)
                        {
                            nbBilles[CouleurBille::ROUGE] = NB_BILLES_ROUGE;
                            nbBilles[CouleurBille::JAUNE] = NB_BILLES_JAUNE;

                            // Les leds de la carte
                            digitalWrite(GPIO_LED_ROUGE, LOW);
                            digitalWrite(GPIO_LED_ORANGE, LOW);
                            digitalWrite(GPIO_LED_VERTE, HIGH);
                            // L'afficheur OLED
                            reinitialiserAffichage();
                            afficheur.setMessageLigne(
                              Afficheur::Ligne1,
                              String("Partie demarree"));
                            afficheur.setMessageLigne(
                              Afficheur::Ligne2,
                              champNumeroJoueur + String(" - ") + etatTour);
                            afficherScore(); // Ligne 3 et 4
                            afficheur.afficher();
                            // active la détection de l'empochage
                            etatPartie = EnCours;
#ifdef DEBUG
                            Serial.println("--> Nouvelle partie");
#endif
                        }
                        else
                        {
                            // L'afficheur OLED
                            afficheur.setMessageLigne(
                              Afficheur::Ligne1,
                              String("Partie en cours"));
                            afficheur.setMessageLigne(
                              Afficheur::Ligne2,
                              champNumeroJoueur + String(" - ") + etatTour);
                            // tir valide ?
                            if(etatTour == String(CHAMP_ETAT_VALIDE))
                            {
                                calculerScore();
                            }
                            afficherScore(); // Ligne 3 et 4
                            afficheur.afficher();
#ifdef DEBUG
                            Serial.println("--> Partie en cours");
#endif
                        }
                    }
                    else if(etat == 0)
                    {
                        if(etatPartie == EnCours)
                        {
                            // désactive la détection de l'empochage
                            etatPartie   = Arretee;
                            tirEncours   = false;
                            numeroJoueur = 0;
                            // Les leds de la carte
                            digitalWrite(GPIO_LED_ROUGE, HIGH);
                            digitalWrite(GPIO_LED_ORANGE, LOW);
                            digitalWrite(GPIO_LED_VERTE, LOW);
                            // L'afficheur OLED
                            reinitialiserAffichage();
                            afficheur.setMessageLigne(Afficheur::Ligne1,
                                                      String("Partie finie"));
                            afficheur.afficher();
#ifdef DEBUG
                            Serial.println("--> Partie finie");
#endif
                        }
                    }
                    else
                    {
#ifdef DEBUG
                        Serial.println("Etat de la manche non défini");
#endif
                    }
                }
            }
        }
        else
        {
#ifdef DEBUG
            Serial.println("Trame invalide");
#endif
        }
    }
}
