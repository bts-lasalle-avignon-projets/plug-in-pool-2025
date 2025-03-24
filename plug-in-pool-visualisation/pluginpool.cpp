#include "pluginpool.h"
#include "communicationbluetooth.h"
#include <QDebug>

PlugInPool::PlugInPool(QObject* parent) :
    QObject(parent), communicationBluetooth(new CommunicationBluetooth(this))
{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;
}

PlugInPool::~PlugInPool()
{
    qDebug() << Q_FUNC_INFO << this;
}
