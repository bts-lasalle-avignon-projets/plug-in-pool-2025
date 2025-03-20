#ifndef JOUEUR_H
#define JOUEUR_H

#include "couleurbille.h"
#include <QString>

class Empoche;

class Joueur
{
  private:
    QString*     nom;
    QString*     prenom;
    int          points;
    int          id;
    CouleurBille couleur;
    int          nbBillesRestantes = 7;

  public:
    Joueur(const QString& nom,
           const QString& prenom,
           int            id,
           CouleurBille   couleur);

    void empocheBille(const Empoche& empoche);
};

#endif // JOUEUR_H
