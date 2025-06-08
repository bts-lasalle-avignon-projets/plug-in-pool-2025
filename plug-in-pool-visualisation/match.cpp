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

int Match::getTirsJoueur(int idJoueur) const
{
    return joueurs[idJoueur].getTirs();
}

int Match::getBillesBlanchesEmpocheesJoueur(int idJoueur) const
{
    return joueurs[idJoueur].getNbBillesBlanchesEmpochees();
}

int Match::getBillesEmpocheesJoueur(int idJoueur) const
{
    return joueurs[idJoueur].getNbBillesEmpochees();
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

void Match::augmenterCompteurTirs(int idJoueur)
{
    joueurs[idJoueur].ajouterTirs();
}

void Match::augmenterCompteurBillesBlanchesEmpochees(int idJoueur)
{
    joueurs[idJoueur].ajouterBilleBlancheEmpochee();
}

void Match::augmenterCompteurBillesEmpochees(int idJoueur)
{
    joueurs[idJoueur].ajouterBillesEmpochees();
}

void Match::reinitialiserTirsJoueurs()
{
    for(Joueur& joueur: joueurs)
    {
        joueur.reinitialiserTirs();
    }
}

void Match::reinitialiserNbBillesBlanchesEmpocheesJoueurs()
{
    for(Joueur& joueur: joueurs)
    {
        joueur.reinitialiserBillesBlanchesEmpochees();
    }
}

void Match::reinitialiserNbBillesEmpocheesJoueurs()
{
    for(Joueur& joueur: joueurs)
    {
        joueur.reinitialiserBillesEmpochees();
    }
}

void Match::attribuerCouleur(int idJoueur, CouleurBille couleurEmpochee)
{
    joueurs[idJoueur].setCouleur(couleurEmpochee);
    int autreJoueur = (idJoueur == 0) ? 1 : 0;
    joueurs[autreJoueur].setCouleur(couleurEmpochee == ROUGE ? JAUNE : ROUGE);
}

void Match::augmenterCompteurEmpochagesReussis(int          idJoueur,
                                               CouleurBille couleurEmpochee)
{
    if(couleurEmpochee == joueurs[idJoueur].getCouleur())
    {
        augmenterCompteurBillesEmpochees(idJoueur);
    }
}
