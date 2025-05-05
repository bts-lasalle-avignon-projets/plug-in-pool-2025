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

    connect(serveur, SIGNAL(newConnection()), this, SLOT(connecterClient()));

    QBluetoothUuid uuid(QBluetoothUuid::Rfcomm);

    serveur->listen(uuid, "BluetoothPlugInPool^");
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    qDebug() << Q_FUNC_INFO << this;
    if(socket != nullptr)
    {
        if(socket->isOpen())
        {
            socket->close();
        }
        delete socket;
    }

    delete serveur;
}

void CommunicationBluetooth::connecterClient()
{
    socket = serveur->nextPendingConnection();

    if(socket)
    {
        qDebug() << Q_FUNC_INFO << socket->peerName()
                 << socket->peerAddress().toString();
        connect(socket,
                SIGNAL(disconnected()),
                this,
                SLOT(deconnecterClient()));
        connect(socket, SIGNAL(readyRead()), this, SLOT(lireTrame()));
        emit connexionClient(true);
    }
}

void CommunicationBluetooth::deconnecterClient()
{
    if(socket)
    {
        qDebug() << Q_FUNC_INFO;
        disconnect(socket,
                   SIGNAL(disconnected()),
                   this,
                   SLOT(deconnecterClient()));
        disconnect(socket, SIGNAL(readyRead()), this, SLOT(lireTrame()));
        emit connexionClient(false);
    }
}

void CommunicationBluetooth::lireTrame()
{
    if(!socket)
        return;

    QByteArray donneesRecues = socket->readAll();
    QString    trame         = QString::fromUtf8(donneesRecues).trimmed();

    qDebug() << Q_FUNC_INFO << "trame" << trame;

    if(trame.startsWith(ENTETE_TRAME) && trame.endsWith(DELIMITEUR_FIN_TRAME))
    {
        QStringList contenuTrame =
          trame.mid(1, trame.length() - 2).split(SEPARATEUR_TRAME);
        qDebug() << Q_FUNC_INFO << "contenuTrame" << contenuTrame;

        if(contenuTrame.size() > 0)
        {
            QString type = contenuTrame[POSITION_TYPE_TRAME];
            qDebug() << Q_FUNC_INFO << "type" << type;

            switch(type.at(0).toLatin1())
            {
                case TRAME_RENCONTRE:
                {
                    int nbManches = type.at(1).toLatin1() - '0';
                    int numeroTable =
                      contenuTrame[POSITION_NUMERO_TABLE].toInt();
                    QString prenomJoueur1 =
                      contenuTrame[POSITION_PRENOM_JOUEUR_1];
                    QString prenomJoueur2 =
                      contenuTrame[POSITION_PRENOM_JOUEUR_2];
                    qDebug()
                      << Q_FUNC_INFO << "numeroTable" << numeroTable
                      << "prenomJoueur1" << prenomJoueur1 << "prenomJoueur2"
                      << prenomJoueur2 << "nbManches" << nbManches;
                    emit trameRencontreRecue(numeroTable,
                                             prenomJoueur1,
                                             prenomJoueur2,
                                             nbManches);
                    break;
                }
                case TRAME_MANCHE:
                {
                    break;
                }
                case TRAME_CHANGEMENT_JOUEUR:
                {
                    break;
                }
                case TRAME_EMPOCHAGE:
                {
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
    }
}
