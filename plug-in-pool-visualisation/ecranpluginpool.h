#ifndef ECRANPLUGINPOOL_H
#define ECRANPLUGINPOOL_H

#include <QWidget>
#include "pluginpool.h"

class EcranPlugInPool : public QWidget
{
    Q_OBJECT

  public:
    EcranPlugInPool(QWidget* parent = nullptr);
    ~EcranPlugInPool();
};
#endif // ECRANPLUGINPOOL_H
