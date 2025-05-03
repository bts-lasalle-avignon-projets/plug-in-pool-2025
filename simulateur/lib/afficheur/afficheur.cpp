/**
*
* @file lib/afficheur/afficheur.cpp
*
* @brief Définition de la classe Afficheur
*
* @author Thierry Vaira
*
* @version 0.1
*
*/
#include <afficheur.h>

/**
* @brief Constructeur de la classe Afficheur
*
* @fn Afficheur::Afficheur
*
* @param adresseI2C int l'adresse I2C de l'afficheur
* @param brocheSDA int broche I2C SDA
* @param brocheSCL int broche I2C SCL
*/
Afficheur::Afficheur(int adresseI2C, int brocheSDA, int brocheSCL) : SSD1306Wire(adresseI2C, brocheSDA, brocheSCL)
{
  this->stitre = "=====================";
}

/**
* @brief Initialise l'afficheur SSD1306Wire
*
* @fn Afficheur::initialiser
*
*/
void Afficheur::initialiser()
{
  init();
  flipScreenVertically();
  setBrightness(15);
  setFont(ArialMT_Plain_10);
  clear();
  setTextAlignment(TEXT_ALIGN_LEFT);
}

/**
* @brief Efface l'afficheur
*
* @fn Afficheur::effacer
*
*/
void Afficheur::effacer()
{
  clear();
}

/**
* @brief Affiche les différentes lignes de l'afficheur
*
* @fn Afficheur::afficher
*
*/
void Afficheur::afficher()
{
  clear();
  afficherTitre();
  for(int i = Ligne1; i < NbLignes; i++)
  {
    afficherMessage(LIGNE_2 + i*HAUTEUR_LIGNE, messages[i]);
  }
  display();
}

/**
* @brief Fixe un titre à afficher
*
* @fn Afficheur::setTitre
*
* @param titre String le titre à afficher
*/
void Afficheur::setTitre(String titre)
{
  this->titre = titre;
}

/**
* @brief Fixe un sous-titre à afficher
*
* @fn Afficheur::setSTitre
*
* @param stitre String le sous-titre à afficher
*/
void Afficheur::setSTitre(String stitre)
{
  this->stitre = stitre;
}

/**
* @brief Fixe un message à un numéro de ligne
*
* @fn Afficheur::setMessageLigne
*
* @param ligne Ligne numéro de ligne pour le message
* @param message String le message à afficher sur le numéro de ligne correspondant
*/
void Afficheur::setMessageLigne(Ligne ligne, String message)
{
  if(ligne >= Ligne1 && ligne < NbLignes)
  {
    messages[ligne] = message;
  }
}

String Afficheur::getMessageLigne(Ligne ligne)
{
  if(ligne >= Ligne1 && ligne < NbLignes)
  {
    return messages[ligne];
  }
  return String("");
}

// Méthodes privées

/**
* @brief Affiche un titre sur l'afficheur
*
* @fn Afficheur::afficherTitre
*
*/
void Afficheur::afficherTitre()
{
  if(titre.length() != 0)
  {
    drawString(0, LIGNE_0, titre);
    drawString(0, LIGNE_1, stitre);
  }
}

/**
* @brief Affiche un message sur une ligne de l'afficheur
*
* @fn Afficheur::afficherMessage
*
* @param ligne int numéro de ligne pour le message
* @param message String le message à afficher sur le numéro de ligne correspondant
*/
void Afficheur::afficherMessage(int ligne, String message)
{
  drawString(0, ligne, message);
}
