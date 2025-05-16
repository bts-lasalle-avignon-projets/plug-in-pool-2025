#include "match.h"
#include <QDebug>

Match::Match(const QString& nom) :
    nom(nom), nbManchesGagnantes(NB_MANCHES),
    horodatage(QDateTime::currentDateTime()), etat(EtatMatch::Cree),
    gagnant(AUCUN_GAGNANT)
{
    qDebug() << Q_FUNC_INFO << this << "nom" << nom;
    joueurActif = 0;
}

void Match::setNbManchesGagnantes(int nbManchesGagnantes)
{
    this->nbManchesGagnantes = nbManchesGagnantes;
}

QString Match::getPrenomJoueur(int idJoueur) const
{
    return joueurs[idJoueur].getPrenom();
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
}
