#include "ecranaccueil.h"
#include <QFile>
#include <QTextStream>
#include <QDebug>

EcranAccueil::EcranAccueil(QWidget* parent) : QWidget(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    setAutoFillBackground(true);

    imageFondEcranAccueil = new QLabel(this);
    afficherCreateurs     = new QLabel(CREATEUR_APPLICATION, this);
    afficherVersion       = new QLabel("v1.0", this);
    connexionBluetooth    = new QLabel("connexion bluetooth", this);
    configurationPartie   = new QLabel("configuration partie", this);

    connexionBluetooth->setObjectName("connexionBluetooth");

    ecranAccueil              = new QVBoxLayout(this);
    espaceCreateursVersion    = new QHBoxLayout();
    espaceConnexionBluetooth  = new QHBoxLayout();
    espaceConfigurationPartie = new QHBoxLayout();

    espaceCreateursVersion->addWidget(afficherCreateurs);
    espaceCreateursVersion->addStretch();
    espaceCreateursVersion->addWidget(afficherVersion);

    espaceConnexionBluetooth->addStretch();
    espaceConnexionBluetooth->addWidget(connexionBluetooth);
    espaceConnexionBluetooth->addStretch();

    espaceConfigurationPartie->addStretch();
    espaceConfigurationPartie->addWidget(configurationPartie);
    espaceConfigurationPartie->addStretch();

    ecranAccueil->addLayout(espaceCreateursVersion);
    ecranAccueil->addStretch();
    ecranAccueil->addLayout(espaceConnexionBluetooth);
    ecranAccueil->addLayout(espaceConfigurationPartie);
    ecranAccueil->addStretch();
}
