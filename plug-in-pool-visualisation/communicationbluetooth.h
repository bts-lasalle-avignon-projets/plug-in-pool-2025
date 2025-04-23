#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>
#include <QBluetoothLocalDevice>
#include <QBluetoothAddress>
#include <QBluetoothSocket>
#include <QBluetoothServer>

#define ENTETE_TRAME         "$"
#define SEPARATEUR_TRAME     "/"
#define DELIMITEUR_FIN_TRAME "!"

#define TRAME_RENCONTRE         'R'
#define TRAME_MANCHE            'M'
#define TRAME_CHANGEMENT_JOUEUR 'C'
#define TRAME_EMPOCHAGE         'E'

#define POSITION_TYPE_TRAME 0
#define POSITION_JOUEUR_1   2
#define POSITION_JOUEUR_2   3

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
    void trameRencontreRecue(QString joueur1, QString joueur2);

  private slots:
    void nouveauClient();
    void lireTrame();
};

#endif // COMMUNICATIONBLUETOOTH_H
