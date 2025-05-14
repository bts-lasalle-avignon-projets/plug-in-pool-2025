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

#define TRAME_RENCONTRE          'D'
#define POSITION_NB_MANCHES      1
#define POSITION_PRENOM_JOUEUR_1 2
#define POSITION_PRENOM_JOUEUR_2 3

#define TRAME_CASSE            'C'
#define POSITION_ID_PARTIE     1
#define POSITION_ID_JOUEUR     2
#define POSITION_COULEUR_BILLE 3
#define POSITION_ID_POCHE      4

#define TRAME_MANCHE    'M'
#define TRAME_EMPOCHAGE 'E'

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
    void trameRencontreRecue(int     nbManches,
                             QString prenomJoueur1,
                             QString prenomJoueur2);
    void trameCasseRecue(int  idPartie,
                         int  idJoueur,
                         char couleurBille,
                         int  idPoche);

  private slots:
    void connecterClient();
    void deconnecterClient();
    void lireTrame();
};

#endif // COMMUNICATIONBLUETOOTH_H
