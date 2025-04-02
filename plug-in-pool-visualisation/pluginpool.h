#ifndef PLUGINPOOL_H
#define PLUGINPOOL_H

#include <QObject>
#include "communicationbluetooth.h"
#include "ecranpluginpool.h"

class Match;

class PlugInPool : public QObject
{
    Q_OBJECT
  private:
    CommunicationBluetooth* communicationBluetooth;
    Match*                  match;
    EcranAccueil*           ecranAccueil;

  public:
    PlugInPool(QObject* parent = nullptr);
    virtual ~PlugInPool();

  private slots:
    void onClientConnecte();
};

#endif // PLUGINPOOL_H
