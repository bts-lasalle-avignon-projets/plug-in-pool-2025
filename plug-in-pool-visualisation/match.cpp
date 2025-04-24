#include "match.h"
#include <QDebug>

Match::Match(const QString& nom) :
    nom(nom), nbManchesGagnantes(nbManchesGagnantes),
    horodatage(QDateTime::currentDateTime()), etat(EtatMatch::Cree),
    gagnant(AUCUN_GAGNANT)
{
}

void Match::enregistrerJoueurs(const QString& nomJoueur1,
                               const QString& nomJoueur2)
{
    joueurs.clear();

    for(int i = 0; i < NB_JOUEURS; ++i)
    {
        QString nom    = (i == 0) ? nomJoueur1 : nomJoueur2;
        QString prenom = "";
        Joueur  joueur(nom, prenom, i);
        joueurs.append(joueur);
    }

    for(const Joueur& joueur: joueurs)
    {
        qDebug() << "Joueur:" << joueur.getNom() << "| ID:" << joueur.getId();
    }
}
