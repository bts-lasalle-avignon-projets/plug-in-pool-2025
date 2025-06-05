#ifndef MATCH_H
#define MATCH_H

#include <QString>
#include <QDateTime>
#include <QList>
#include "joueur.h"
#include "manche.h"

#define NB_MANCHES 3

#define AUCUN_GAGNANT -1

#define NB_BILLES_JOUEUR 7
#define NB_BOULES_ROUGES 7
#define NB_BOULES_JAUNES 7

#define CONDITION_VICTOIRE_BILLES     7
#define CONDITION_VICTOIRE_BILLE_NOIR 1

/*TODO ( adapté à la trame )
#define FAUTE_BILLE_BLANCHE ?
#define FAUTE_BILLE_NOIRE ?
#define FAUTE_BILLE_MAUVAISE_COULEUR ?
*/

#define NB_POCHES 6

class Match
{
  private:
    enum EtatMatch
    {
        Cree    = 0,
        EnCours = 1,
        Fini    = 2
    };

    QString       nom;
    int           nbManchesGagnantes;
    QDateTime     horodatage;
    EtatMatch     etat;
    int           gagnant;
    QList<Joueur> joueurs;
    QList<Manche> manches;
    int           joueurActif;

  public:
    Match(const QString& nom);

    void    setNbManchesGagnantes(int nbManchesGagnantes);
    void    enregistrerJoueurs(const QString& prenomJoueur1,
                               const QString& prenomJoueur2);
    void    augmenterCompteurTirs(int idJoueur);
    void    augmenterCompteurBillesBlanchesEmpochees(int idJoueur);
    void    augmenterCompteurBillesEmpochees(int idJoueur);
    void    reinitialiserTirsJoueurs();
    void    reinitialiserNbBillesBlanchesEmpocheesJoueurs();
    void    reinitialiserNbBillesEmpocheesJoueurs();
    void    attribuerCouleur(int idJoueur, CouleurBille couleurEmpochee);
    void    augmenterCompteurEmpochagesReussis(int          idJoueur,
                                               CouleurBille couleurEmpochee);
    QString getPrenomJoueur(int idJoueur) const;
    int     getTirsJoueur(int idJoueur) const;
    int     getBillesBlanchesEmpocheesJoueur(int idJoueur) const;
    int     getBillesEmpocheesJoueur(int idJoueur) const;
};

#endif // MATCH_H
