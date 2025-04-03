#include "ecranaccueil.h"
#include <QFile>
#include <QTextStream>
#include <QDebug>

EcranAccueil::EcranAccueil(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageCreateurs  = new QLabel(CREATEUR_APPLICATION, ecran);
    affichageVersion    = new QLabel("v1.0", ecran);
    connexionBluetooth  = new QLabel("Attente connexion Bluetooth", ecran);
    configurationPartie = new QLabel("Configuration de la partie", ecran);

    affichageCreateurs->setObjectName("affichageCreateurs");
    affichageVersion->setObjectName("affichageVersion");
    connexionBluetooth->setObjectName("connexionBluetooth");
    configurationPartie->setObjectName("configurationPartie");

    QVBoxLayout* ecranAccueil              = new QVBoxLayout(ecran);
    QHBoxLayout* espaceCreateursVersion    = new QHBoxLayout();
    QHBoxLayout* espaceConnexionBluetooth  = new QHBoxLayout();
    QHBoxLayout* espaceConfigurationPartie = new QHBoxLayout();

    espaceCreateursVersion->addWidget(affichageCreateurs);
    espaceCreateursVersion->addStretch();
    espaceCreateursVersion->addWidget(affichageVersion);

    espaceConnexionBluetooth->addStretch();
    espaceConnexionBluetooth->addWidget(connexionBluetooth);
    espaceConnexionBluetooth->addStretch();

    espaceConfigurationPartie->addStretch();
    espaceConfigurationPartie->addWidget(configurationPartie);
    espaceConfigurationPartie->addStretch();

    /**
     * @todo Est-ce vraiment la bonne technique ?
     */
    ecranAccueil->addLayout(espaceCreateursVersion);
    ecranAccueil->addSpacing(700);
    ecranAccueil->addLayout(espaceConnexionBluetooth);
    ecranAccueil->addSpacing(80);
    ecranAccueil->addLayout(espaceConfigurationPartie);
    ecranAccueil->addStretch();
}

EcranAccueil::~EcranAccueil()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranAccueil::getEcran() const
{
    return ecran;
}

QLabel* EcranAccueil::getConnexionBluetoothLabel() const

{
    qDebug() << "Adresse QLabel (EcranAccueil): " << connexionBluetooth;
    return connexionBluetooth;
}

QLabel* EcranAccueil::getConfigurationPartieLabel() const

{
    qDebug() << "Adresse QLabel (EcranAccueil): " << configurationPartie;
    return configurationPartie;
}
