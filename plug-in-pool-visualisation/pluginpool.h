#ifndef PLUGINPOOL_H
#define PLUGINPOOL_H

#include <QObject>

class CommunicationBluetooth;
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
};

#endif // PLUGINPOOL_H
