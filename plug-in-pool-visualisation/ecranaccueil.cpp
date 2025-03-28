#include "ecranaccueil.h"
#include <QFile>
#include <QTextStream>

EcranAccueil::EcranAccueil(QWidget* parent) : QWidget(parent)
{
    QFile file(":/pluginpool.css");
    if(file.open(QFile::ReadOnly | QFile::Text))
    {
        QTextStream in(&file);
        QString     style = in.readAll();
        this->setStyleSheet(style); // Appliquer le CSS
    }

    imageFondEcranAccueil = new QLabel(this);
    afficherCreateurs     = new QLabel(CREATEUR_APPLICATION, this);
    afficherVersion       = new QLabel("v1.0", this);
    connexionBluetooth    = new QLabel("connexion bluetooth", this);
    configurationPartie   = new QLabel("configuration partie", this);

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

    setLayout(ecranAccueil);
}
