#ifndef ECRANMATCH_H
#define ECRANMATCH_H

#include <QtWidgets>

#define NB_POCHES 6

const int lignesPoches[NB_POCHES]   = { 0, 0, 0, 2, 2, 2 };
const int colonnesPoches[NB_POCHES] = { 4, 2, 0, 5, 3, 1 };

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

    QLabel* getJoueurUnLabel() const;
    QLabel* getJoueurDeuxLabel() const;
    QLabel* getNumeroTableLabel() const;
    void    demarrerChronometre();
    void    genererBoules();
    void    initialiserPochesTable();

  signals:
};

#endif // ECRANMATCH_H
