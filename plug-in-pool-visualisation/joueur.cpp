#include "joueur.h"
#include "match.h"
#include <QDebug>

Joueur::Joueur(const QString& nom,
               const QString& prenom,
               int            id,
               CouleurBille   couleur) :
    nom(nom),
    prenom(prenom), id(id), couleur(couleur),
    nbBillesRestantes(NB_BILLES_JOUEUR)
{
    qDebug() << Q_FUNC_INFO << this << "nom" << nom << "prenom" << prenom
             << "id" << id << "couleur" << couleur;
}
