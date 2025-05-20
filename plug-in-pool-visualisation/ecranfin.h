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

  public:
    explicit EcranFin(QWidget* parent = nullptr);
    virtual ~EcranFin();
    QWidget* getEcran() const;
    void     afficherJoueurGagnant(QString PrenomJoueurGagnant);

  signals:
};

#endif // ECRANFIN_H
