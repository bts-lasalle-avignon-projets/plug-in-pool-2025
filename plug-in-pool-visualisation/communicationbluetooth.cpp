#include "communicationbluetooth.h"
#include <QDebug>
#include <QBluetoothUuid>

CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent), serveur(nullptr), socket(nullptr)
{
    qDebug() << Q_FUNC_INFO;
    if(!peripheriqueLocal.isValid())

    {
        qWarning() << "Bluetooth désactivé !";

        return;
    }

    peripheriqueLocal.powerOn();

    serveur = new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this);

    connect(serveur, SIGNAL(newConnection()), this, SLOT(nouveauClient()));

    QBluetoothUuid uuid(QBluetoothUuid::Rfcomm);

    serveur->listen(uuid, "MyBluetoothService");
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    qDebug() << Q_FUNC_INFO << this;
    if(socket)
    {
        socket->close();
        delete socket;
    }

    delete serveur;
}

void CommunicationBluetooth::nouveauClient()
{
    qDebug() << "Tentative de connexion d'un nouveau client...";
    socket = serveur->nextPendingConnection();

    if(socket)
    {
        qDebug() << "Client connecté avec succès!";
        connect(socket, SIGNAL(disconnected()), socket, SLOT(deleteLater()));
        emit clientConnecte();
    }
}
