#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>
#include <QBluetoothServer>
#include <QBluetoothSocket>
#include <QBluetoothServiceInfo>

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  private:
    QBluetoothServer* serveurBluetooth;
    QBluetoothSocket* socketBluetooth;

  public:
    explicit CommunicationBluetooth(QObject* parent = nullptr);
    virtual ~CommunicationBluetooth();

  signals:
    void appareilConnecte(QString nomAppareil);

  private slots:
    void nouvelleConnexion();
    void deconnexionClient();
};

#endif // COMMUNICATIONBLUETOOTH_H
