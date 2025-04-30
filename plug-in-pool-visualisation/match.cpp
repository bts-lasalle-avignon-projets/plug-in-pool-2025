#include "match.h"
#include <QDebug>

Match::Match(const QString& nom) :
    nom(nom), nbManchesGagnantes(nbManchesGagnantes),
    horodatage(QDateTime::currentDateTime()), etat(EtatMatch::Cree),
    gagnant(AUCUN_GAGNANT)
{
}

void Match::enregistrerJoueurs(const QString& prenomJoueur1,
                               const QString& prenomJoueur2)
{
    joueurs.clear();

    for(int i = 0; i < NB_JOUEURS; ++i)
    {
        QString prenom = (i == 0) ? prenomJoueur1 : prenomJoueur2;
        QString nom    = "";
        Joueur  joueur(nom, prenom, i);
        joueurs.append(joueur);
    }

    for(const Joueur& joueur: joueurs)
    {
        qDebug() << "Joueur:" << joueur.getNom() << "| ID:" << joueur.getId();
    }
}
