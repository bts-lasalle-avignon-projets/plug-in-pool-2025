#ifndef ECRANMATCH_H
#define ECRANMATCH_H

#include <QtWidgets>

#define TEMPS_INCREMENTATION 1000 // ms

#define MINUTE          60 // s
#define LARGEUR_MINUTE  2
#define LARGEUR_SECONDE 2
#define BASE_DECIMALE   10

class EcranMatch : public QWidget
{
    Q_OBJECT
  private:
    QWidget* ecran;
    QLabel*  affichageJoueurUn;
    QLabel*  affichageJoueurDeux;
    QLabel*  affichageNombreParties;
    QTimer*  compteAReboursDebutMatch;
    QLabel*  affichageCompteAReboursDebutMatch;
    QLabel*  affichageFondCompteAReboursDebutMatch;
    int      secondesEcoulees;
    int      secondesRestantes;

  public:
    explicit EcranMatch(QWidget* parent = nullptr);
    virtual ~EcranMatch();
    QWidget* getEcran() const;

    void afficherInformationsMatch(int     nbManches,
                                   QString joueur1,
                                   QString joueur2);
    void demarrerCompteAReboursDebutMatch(int dureeEnSecondes);
    void positionnerAffichageJoueurs();

  signals:
    void compteAReboursDebutMatchTermine();
};

#endif // ECRANMATCH_H
