#ifndef PLUGINPOOL_H
#define PLUGINPOOL_H

#include <QObject>
#include <QTimer>

#define TEMPS_AVANT_LANCEMENT_RENCONTRE 5000

class CommunicationBluetooth;
class Match;
class EcranPlugInPool;

class PlugInPool : public QObject
{
    Q_OBJECT
  private:
    CommunicationBluetooth* communicationBluetooth;
    Match*                  match;
    EcranPlugInPool*        ecranPlugInPool;

  public:
    PlugInPool(QObject* parent = nullptr);
    virtual ~PlugInPool();

  private slots:
    void gererConnexion(bool etat);
    void configurerMatch(int     numeroTable,
                         QString prenomJoueur1,
                         QString prenomJoueur2,
                         int     nbManches);
    void changerEcranMatch();
};

#endif // PLUGINPOOL_H
