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
    int            tirs;
    int            id;
    CouleurBille   couleur;
    int            nbBillesEmpochees;
    int            nbBillesBlanchesEmpochees;
    QList<Empoche> empochees;

  public:
    Joueur(const QString& nom, const QString& prenom, int id);

    QString      getNom() const;
    QString      getPrenom() const;
    int          getId() const;
    int          getPoints() const;
    int          getTirs() const;
    CouleurBille getCouleur() const;
    int          getNbBillesEmpochees() const;
    int          getNbBillesBlanchesEmpochees() const;
    void         setCouleur(CouleurBille couleur);
    void         ajouterTirs();
    void         ajouterBilleBlancheEmpochee();
    void         ajouterBillesEmpochees();
    void         reinitialiserTirs();
    void         reinitialiserBillesBlanchesEmpochees();
    void         reinitialiserBillesEmpochees();
};

#endif // JOUEUR_H
