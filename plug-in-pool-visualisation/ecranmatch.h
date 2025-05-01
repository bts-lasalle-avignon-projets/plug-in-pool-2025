#ifndef ECRANMATCH_H
#define ECRANMATCH_H

#include <QtWidgets>

#define NB_POCHES            6
#define TEMPS_INCREMENTATION 1000 // ms

#define NB_BOULES_ROUGES 7
#define NB_BOULES_JAUNES 7

#define MINUTE          60 // s
#define LARGEUR_MINUTE  2
#define LARGEUR_SECONDE 2
#define BASE_DECIMALE   10

const int lignesPoches[NB_POCHES]   = { 0, 2, 0, 2, 0, 2 };
const int colonnesPoches[NB_POCHES] = { 2, 2, 1, 1, 0, 0 };

class EcranMatch : public QObject
{
    Q_OBJECT
  private:
    QWidget*         ecran;
    QLabel*          affichageJoueurUn;
    QLabel*          affichageJoueurDeux;
    QTimer*          chronometre;
    QTimer*          compteAReboursManche;
    QLabel*          labelChronometre;
    QLabel*          labelCompteAReboursManche;
    QLabel*          affichageNumeroTable;
    QLabel*          affichageNomJeu;
    int              secondesEcoulees;
    QVector<QLabel*> boulesRouges;
    QVector<QLabel*> boulesJaunes;

    QHBoxLayout* espaceBoulesRouges;
    QHBoxLayout* espaceBoulesJaunes;
    QHBoxLayout* espaceBouleBlanche;
    QHBoxLayout* espaceBouleNoir;

    QGridLayout* espaceTableBillard;

    QLabel* bouleRougeImagePoche[NB_POCHES];
    QLabel* bouleJauneImagePoche[NB_POCHES];
    QLabel* compteurBoulesRougesPoche[NB_POCHES];
    QLabel* compteurBoulesJaunesPoche[NB_POCHES];

  public:
    explicit EcranMatch(QWidget* parent = nullptr);
    virtual ~EcranMatch();
    QWidget* getEcran() const;

    void afficherInformationsMatch(int     numeroTable,
                                   QString joueur1,
                                   QString joueur2,
                                   int     nbManches);
    void demarrerChronometre();
    void genererBoules();
    void initialiserPochesTable();

  signals:
};

#endif // ECRANMATCH_H
