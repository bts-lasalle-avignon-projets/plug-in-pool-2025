#ifndef ECRANACCUEIL_H
#define ECRANACCUEIL_H

#include <QtWidgets>

#define CREATEUR_APPLICATION "NAVARRO Matteo"

class EcranAccueil : public QObject
{
    Q_OBJECT
  private:
    QWidget* ecran;
    QLabel*  affichageCreateurs;
    QLabel*  affichageVersion;
    QLabel*  connexionBluetooth;
    QLabel*  configurationPartie;

  public:
    explicit EcranAccueil(QWidget* parent = nullptr);
    virtual ~EcranAccueil();
    QWidget* getEcran() const;

    QLabel* getConnexionBluetoothLabel() const;
    QLabel* getConfigurationPartieLabel() const;

  signals:

  public slots:
};

#endif // ECRANACCUEIL_H
