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
    ecranAccueil->getConnexionBluetoothLabel()->setText("Connecté");
}
