#include "joueur.h"
#include "match.h"
#include <QDebug>

Joueur::Joueur(const QString& nom, const QString& prenom, int id) :
    nom(nom), prenom(prenom), id(id), couleur(couleur), points(0),
    nbBillesRestantes(NB_BILLES_JOUEUR)
{
    qDebug() << Q_FUNC_INFO << this << "nom" << nom << "prenom" << prenom
             << "id" << id << "couleur" << couleur;
}

QString Joueur::getNom() const
{
    return nom;
}

QString Joueur::getPrenom() const
{
    return prenom;
}

int Joueur::getId() const
{
    return id;
}

int Joueur::getPoints() const
{
    return points;
}

CouleurBille Joueur::getCouleur() const
{
    return couleur;
}

int Joueur::getNbBillesRestantes() const
{
    return nbBillesRestantes;
}
