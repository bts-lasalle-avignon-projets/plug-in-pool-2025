#ifndef ECRANACCUEIL_H
#define ECRANACCUEIL_H

#include <QWidget>
#include <QLabel>
#include <QVBoxLayout>

#define CREATEUR_APPLICATION "NAVARRO Matteo"

class EcranAccueil : public QWidget
{
    Q_OBJECT
  private:
    QLabel* imageFondEcranAccueil;
    QLabel* afficherCreateurs;
    QLabel* afficherVersion;
    QLabel* connexionBluetooth;
    QLabel* configurationPartie;

    QVBoxLayout* ecranAccueil;
    QHBoxLayout* espaceCreateursVersion;
    QHBoxLayout* espaceConnexionBluetooth;
    QHBoxLayout* espaceConfigurationPartie;

  public:
    explicit EcranAccueil(QWidget* parent = nullptr);

  signals:
};

#endif // ECRANACCUEIL_H
