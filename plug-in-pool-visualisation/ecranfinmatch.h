#ifndef ECRANFINMATCH_H
#define ECRANFINMATCH_H

#include <QtWidgets>

class EcranFinMatch : public QWidget
{
    Q_OBJECT

  private:
    QWidget* ecran;
    QLabel*  affichageVersion;
    QLabel*  affichageJoueurGagnant;
    QLabel*  affichageScores;

  public:
    explicit EcranFinMatch(QWidget* parent = nullptr);
    virtual ~EcranFinMatch();

    QWidget* getEcran() const;
    void     afficherJoueurGagnant(QString PrenomJoueurGagnant);
    void     afficherScores(QString scores);
  signals:
};

#endif // ECRANFINMATCH_H
