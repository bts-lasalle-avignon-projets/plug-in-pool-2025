#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>
#include <QBluetoothLocalDevice>
#include <QBluetoothAddress>
#include <QBluetoothSocket>
#include <QBluetoothServer>

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  private:
    QBluetoothLocalDevice peripheriqueLocal;
    QBluetoothServer*     serveur;
    QBluetoothSocket*     socket;

  public:
    explicit CommunicationBluetooth(QObject* parent = nullptr);
    virtual ~CommunicationBluetooth();

  signals:
    void clientConnecte();

  private slots:
    void nouveauClient();
};

#endif // COMMUNICATIONBLUETOOTH_H
