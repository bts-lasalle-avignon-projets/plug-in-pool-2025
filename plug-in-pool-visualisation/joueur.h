#ifndef JOUEUR_H
#define JOUEUR_H

#include "couleurbille.h"
#include "empoche.h"
#include <QString>
#include <QList>

#define NB_JOUEURS 2

class Empoche;

class Joueur
{
  private:
    QString        nom;
    QString        prenom;
    int            points;
    int            id;
    CouleurBille   couleur;
    int            nbBillesRestantes;
    QList<Empoche> empochees;

  public:
    Joueur(const QString& nom, const QString& prenom, int id);

    QString      getNom() const;
    QString      getPrenom() const;
    int          getId() const;
    int          getPoints() const;
    CouleurBille getCouleur() const;
    int          getNbBillesRestantes() const;
};

#endif // JOUEUR_H
