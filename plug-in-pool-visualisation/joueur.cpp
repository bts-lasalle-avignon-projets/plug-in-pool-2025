#include "joueur.h"
#include "match.h"
#include <QDebug>

Joueur::Joueur(const QString& nom, const QString& prenom, int id) :
    nom(nom), prenom(prenom), id(id), couleur(CouleurBille::AUCUNE), points(0),
    nbBillesEmpochees(NB_BILLES_JOUEUR)
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

int Joueur::getTirs() const
{
    return tirs;
}

int Joueur::getNbBillesEmpochees() const
{
    return nbBillesEmpochees;
}

int Joueur::getNbBillesBlanchesEmpochees() const
{
    return nbBillesBlanchesEmpochees;
}

void Joueur::setCouleur(CouleurBille couleur)
{
    this->couleur = couleur;
}

void Joueur::ajouterTirs()
{
    tirs++;
}

void Joueur::ajouterBilleBlancheEmpochee()
{
    nbBillesBlanchesEmpochees++;
}

void Joueur::ajouterBillesEmpochees()
{
    nbBillesEmpochees++;
}

void Joueur::reinitialiserTirs()
{
    tirs = 0;
}

void Joueur::reinitialiserBillesBlanchesEmpochees()
{
    nbBillesBlanchesEmpochees = 0;
}

void Joueur::reinitialiserBillesEmpochees()
{
    nbBillesEmpochees = 0;
}
