#ifndef ECRANFIN_H
#define ECRANFIN_H

#include <QtWidgets>

class EcranFin : public QObject
{
    Q_OBJECT
  private:
    QWidget* ecran;
    QLabel*  affichageVersion;
    QLabel*  affichageJoueurGagnant;

    QLabel* affichageJoueurUn;
    QLabel* affichageTirsJoueurUn;
    QLabel* affichageBillesBlanchesEmpocheesJoueurUn;
    QLabel* affichageEmpochagesReussisJoueurUn;

    QLabel* affichageJoueurDeux;
    QLabel* affichageTirsJoueurDeux;
    QLabel* affichageBillesBlanchesEmpocheesJoueurDeux;
    QLabel* affichageEmpochagesReussisJoueurDeux;

  public:
    explicit EcranFin(QWidget* parent = nullptr);
    virtual ~EcranFin();
    QWidget* getEcran() const;
    void     afficherJoueurGagnant(QString PrenomJoueurGagnant);
    void     afficherStatistiques(QString prenomJoueurUn,
                                  QString prenomJoueurDeux,
                                  QString tirsJoueurUn,
                                  QString tirsJoueurDeux,
                                  QString billesBlanchesEmpocheesJoueurUn,
                                  QString billesBlanchesEmpocheesJoueurDeux,
                                  QString billesEmpocheesJoueurUn,
                                  QString billesEmpocheesJoueurDeux);

  signals:
};

#endif // ECRANFIN_H
