#ifndef PLUGINPOOL_H
#define PLUGINPOOL_H

#include <QObject>
#include "communicationbluetooth.h"
#include "ecranpluginpool.h"
#include "QTimer"

#define TEMPS_AVANT_LANCEMENT_RENCONTRE 5000

class Match;

class PlugInPool : public QObject
{
    Q_OBJECT
  private:
    CommunicationBluetooth* communicationBluetooth;
    Match*                  match;
    EcranPlugInPool*        ecranPlugInPool;
    EcranAccueil*           ecranAccueil;

  public:
    PlugInPool(QObject* parent = nullptr);
    virtual ~PlugInPool();

  private slots:
    void bluetoothConnecte();
    void changerEcranMatch();
    void configurationRecu();
};

#endif // PLUGINPOOL_H
