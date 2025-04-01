#ifndef PLUGINPOOL_H
#define PLUGINPOOL_H

#include <QObject>
#include "communicationbluetooth.h"

class Match;

class PlugInPool : public QObject
{
    Q_OBJECT
  private:
    CommunicationBluetooth* communicationBluetooth;
    Match*                  match;

  public:
    PlugInPool(QObject* parent = nullptr);
    virtual ~PlugInPool();

    CommunicationBluetooth* getCommunicationBluetooth() const;
};

#endif // PLUGINPOOL_H
