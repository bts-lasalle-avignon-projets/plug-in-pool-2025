#include "pluginpool.h"
#include "communicationbluetooth.h"
#include <QDebug>

PlugInPool::PlugInPool(QObject* parent) :
    QObject(parent), communicationBluetooth(new CommunicationBluetooth(this)),
    ecranPlugInPool(qobject_cast<EcranPlugInPool*>(parent))

{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    connect(communicationBluetooth,
            &CommunicationBluetooth::clientConnecte,
            this,
            &PlugInPool::bluetoothConnecte);

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameRencontreRecue,
            this,
            &PlugInPool::configurationRecu);
}

PlugInPool::~PlugInPool()
{
    qDebug() << Q_FUNC_INFO << this;
    delete communicationBluetooth;
}

void PlugInPool::bluetoothConnecte()
{
    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    if(ecranAccueil && ecranAccueil->getConnexionBluetoothLabel())
    {
        ecranAccueil->getConnexionBluetoothLabel()->setText(
          "Bluetooth Connecté");
        ecranAccueil->getConnexionBluetoothLabel()->setProperty("class",
                                                                "connecte");
        ecranAccueil->getConnexionBluetoothLabel()->style()->unpolish(
          ecranAccueil->getConnexionBluetoothLabel());
        ecranAccueil->getConnexionBluetoothLabel()->style()->polish(
          ecranAccueil->getConnexionBluetoothLabel());
        ecranAccueil->getConnexionBluetoothLabel()->update();
    }
}

void PlugInPool::configurationRecu()
{
    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    if(ecranAccueil && ecranAccueil->getConfigurationPartieLabel())
    {
        ecranAccueil->getConfigurationPartieLabel()->setText(
          "Configuration Terminée");
        ecranAccueil->getConfigurationPartieLabel()->setProperty("class",
                                                                 "configure");
        ecranAccueil->getConfigurationPartieLabel()->style()->unpolish(
          ecranAccueil->getConfigurationPartieLabel());
        ecranAccueil->getConfigurationPartieLabel()->style()->polish(
          ecranAccueil->getConfigurationPartieLabel());
        ecranAccueil->getConfigurationPartieLabel()->update();
        QTimer::singleShot(TEMPS_AVANT_LANCEMENT_RENCONTRE,
                           this,
                           &PlugInPool::changerEcranMatch);
    }
}

void PlugInPool::changerEcranMatch()
{
    if(ecranPlugInPool)
    {
        qDebug() << "Passage à l'écran de match !";
        ecranPlugInPool->afficherEcranMatch();
    }
}
