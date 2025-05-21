/**
*
* @file lib/afficheur/afficheur.h
*
* @brief Déclaration de la classe Afficheur
*
* @author Thierry Vaira
*
* @version 0.1
*
*/
#ifndef AFFICHEUR_H
#define AFFICHEUR_H

#include <Arduino.h>
#include <Wire.h>
#include "SSD1306Wire.h"

// I2C
#define ADRESSE_I2C_OLED_DEFAUT    0x3c  //!< Adresse I2C par défaut de l'afficheur OLED SSD1306Wire
#define BROCHE_I2C_SDA_DEFAUT      21  //!< Broche SDA
#define BROCHE_I2C_SCL_DEFAUT      22  //!< Broche SCL

#define LIGNE_0           0  //!< coordonnée y de la ligne n°0
#define LIGNE_1           10 //!< coordonnée y de la ligne n°1
#define LIGNE_2           20 //!< coordonnée y de la ligne n°2
#define LIGNE_3           30 //!< coordonnée y de la ligne n°3
#define LIGNE_4           40 //!< coordonnée y de la ligne n°4
#define LIGNE_5           50 //!< coordonnée y de la ligne n°5
#define HAUTEUR_LIGNE     10 //!< Espacement en pixels entre chaque ligne
#define NB_LIGNES         6  //!< Nombre de lignes total dans ce mode
#define NB_LIGNES_MESSAGE 4  //!< Nombre de lignes pour les messages 

/**
* @class Afficheur
*
* @brief Déclaration de la classe Afficheur qui permet de gérer l'écran OLED SSD1306
*
* @author Thierry Vaira
*
* @version 0.1
*
*/
class Afficheur : public SSD1306Wire
{
public:
  Afficheur(int adresseI2C=ADRESSE_I2C_OLED_DEFAUT, int brocheSDA=BROCHE_I2C_SDA_DEFAUT, int brocheSCL=BROCHE_I2C_SCL_DEFAUT);
  /**
   * @enum Ligne
   *
   * @brief Précise les numéros de ligne pour les messages sur l'afficheur
   *
   */
  enum Ligne
  {
    Ligne1 = 0, // Score en cours ROUGE ou JAUNE
    Ligne2 = 1, // Joueur ROUGE
    Ligne3 = 2, // Joueur JAUNE
    Ligne4 = 3, // trame reçue et état de la partie
    NbLignes = 4,
  };
  void initialiser();
  void effacer();
  void afficher();
  void setTitre(String titre);
  void setSTitre(String stitre);
  void setMessageLigne(Ligne ligne, String message);
  String getMessageLigne(Ligne ligne);

private:
  String titre; //!< un titre affiché sur la première ligne de l'écran
  String stitre; //!< un sous-titre affiché sur la première ligne de l'écran
  String messages[NbLignes]; //!< les messages à afficher pour chaque ligne

  void afficherTitre();
  void afficherMessage(int ligne, String message);
};

#endif
