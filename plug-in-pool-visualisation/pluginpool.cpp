#include "pluginpool.h"
#include "communicationbluetooth.h"
#include <QDebug>

PlugInPool::PlugInPool(QObject* parent) :
    QObject(parent), communicationBluetooth(new CommunicationBluetooth(this)),
    ecranAccueil(new EcranAccueil())
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    connect(communicationBluetooth,
            &CommunicationBluetooth::clientConnecte,
            this,
            &PlugInPool::onClientConnecte);
}

PlugInPool::~PlugInPool()
{
    qDebug() << Q_FUNC_INFO << this;
    delete communicationBluetooth;
}

void PlugInPool::onClientConnecte()
{
    if(!ecranAccueil)
    {
        qWarning() << "Erreur: ecranAccueil est NULL !";
        return;
    }
    qDebug() << "Signal clientConnecte() reçu! Mise à jour de l'affichage...";
    if(ecranAccueil && ecranAccueil->getConnexionBluetoothLabel())
    {
        ecranAccueil->getConnexionBluetoothLabel()->setText("Connecté");
        qDebug() << "Texte QLabel changé en 'Connecté'";
        qDebug() << "Adresse QLabel (PlugInPool): "
                 << ecranAccueil->getConnexionBluetoothLabel();
    }
    else
    {
        qDebug() << "Erreur: ecranAccueil ou connexionBluetoothLabel est NULL";
    }
}
