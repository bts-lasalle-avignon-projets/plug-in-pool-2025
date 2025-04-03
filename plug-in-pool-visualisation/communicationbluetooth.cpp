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

    connect(socket, SIGNAL(readyRead()), this, SLOT(lireTrame()));
}

void CommunicationBluetooth::lireTrame()
{
    if(!socket)
        return;

    QByteArray lectureTrame = socket->readAll();
    QString    trame        = QString::fromUtf8(lectureTrame).trimmed();

    qDebug() << "Trame reçue :" << trame;

    if(trame.startsWith(ENTETE_TRAME) && trame.endsWith(DELIMITEUR_FIN_TRAME))
    {
        QStringList contenuTrame =
          trame.mid(1, trame.length() - 2).split(SEPARATEUR_TRAME);

        if(contenuTrame.size() > 0)
        {
            QString type = contenuTrame[POSITION_TYPE_TRAME];
            qDebug() << "Type de trame :" << type;

            switch(type.at(0).toLatin1())
            {
                case TRAME_RENCONTRE:
                    emit trameRencontreRecue();
                    break;

                case TRAME_MANCHE:
                    break;

                case TRAME_CHANGEMENT_JOUEUR:
                    break;

                case TRAME_EMPOCHAGE:
                    break;

                default:
                    break;
            }
        }
    }
}
