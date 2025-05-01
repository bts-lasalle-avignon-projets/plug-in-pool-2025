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

#define POSITION_TYPE_TRAME 0

#define TRAME_RENCONTRE          'R'
#define POSITION_TYPE_TRAME      0
#define POSITION_NUMERO_TABLE    1
#define POSITION_PRENOM_JOUEUR_1 2
#define POSITION_PRENOM_JOUEUR_2 3

#define TRAME_MANCHE            'M'
#define TRAME_CHANGEMENT_JOUEUR 'C'
#define TRAME_EMPOCHAGE         'E'

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
    void connexionClient(bool etat);
    void trameRencontreRecue(int     numeroTable,
                             QString prenomJoueur1,
                             QString prenomJoueur2,
                             int     nbManches);

  private slots:
    void connecterClient();
    void deconnecterClient();
    void lireTrame();
};

#endif // COMMUNICATIONBLUETOOTH_H
