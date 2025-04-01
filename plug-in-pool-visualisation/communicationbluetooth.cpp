#include "communicationbluetooth.h"
#include <QDebug>
#include <QBluetoothUuid>

CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent),
    serveurBluetooth(
      new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this)),
    socketBluetooth(nullptr)
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;
    connect(serveurBluetooth,
            &QBluetoothServer::newConnection,
            this,
            &CommunicationBluetooth::nouvelleConnexion);

    QBluetoothUuid serviceUuid = QBluetoothUuid(QBluetoothUuid::SerialPort);
    serveurBluetooth->listen(serviceUuid);

    qDebug() << "Serveur Bluetooth en attente de connexion...";
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    qDebug() << Q_FUNC_INFO << this;
    delete serveurBluetooth;
}

void CommunicationBluetooth::nouvelleConnexion()
{
    if(socketBluetooth)
    {
        socketBluetooth->deleteLater();
    }

    socketBluetooth = serveurBluetooth->nextPendingConnection();
    if(socketBluetooth)
    {
        connect(socketBluetooth,
                &QBluetoothSocket::disconnected,
                this,
                &CommunicationBluetooth::deconnexionClient);
        qDebug() << "Appareil connecté :" << socketBluetooth->peerName();
        emit appareilConnecte(socketBluetooth->peerName());
    }
}

void CommunicationBluetooth::deconnexionClient()
{
    qDebug() << "Appareil déconnecté";
    emit appareilConnecte("Attente connexion Bluetooth");
}
