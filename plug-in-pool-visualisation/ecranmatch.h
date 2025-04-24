#ifndef ECRANMATCH_H
#define ECRANMATCH_H

#include <QtWidgets>

class EcranMatch : public QObject
{
    Q_OBJECT
  private:
    QWidget* ecran;
    QLabel*  affichageJoueurUn;
    QLabel*  affichageJoueurDeux;
    QTimer*  chronometre;
    QTimer*  compteAReboursManche;
    QLabel*  labelChronometre;
    QLabel*  labelCompteAReboursManche;
    QLabel*  affichageNumeroTable;
    QLabel*  affichageNomJeu;
    int      secondesEcoulees;

  public:
    explicit EcranMatch(QWidget* parent = nullptr);
    virtual ~EcranMatch();
    QWidget* getEcran() const;

    QLabel* getJoueurUnLabel() const;
    QLabel* getJoueurDeuxLabel() const;
    QLabel* getNumeroTableLabel() const;
    void    demarrerChronometre();

  signals:
};

#endif // ECRANMATCH_H
