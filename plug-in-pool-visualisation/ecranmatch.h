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

  public:
    explicit EcranMatch(QWidget* parent = nullptr);
    virtual ~EcranMatch();
    QWidget* getEcran() const;

  signals:
};

#endif // ECRANMATCH_H
