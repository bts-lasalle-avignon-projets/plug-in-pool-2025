/**
 * @file ecranpluginpool.h
 *
 * @brief Déclaration de la classe EcranPlugInPool
 * @author NAVARRO Mattéo
 * @version 1.0
 */

#ifndef ECRANPLUGINPOOL_H
#define ECRANPLUGINPOOL_H

#include <QtWidgets>

/**
 * @def NOM_APPLICATION
 * @brief Le nom de l'application
 */
#define NOM_APPLICATION "PlugInPool"

/**
 * @def VERSION_APPLICATION
 * @brief La version de l'application
 */
#define VERSION_APPLICATION "1.0"

class PlugInPool;

/**
 * @class EcranPlugInPool
 * @brief Déclaration de la classe EcranPlugInPool
 * @details Cette classe gère l'interface graphique de l'application PlugInPool
 */
class EcranPlugInPool : public QWidget
{
    Q_OBJECT
  private:
    PlugInPool* plugInPool; //!< association vers PlugInPool

  public:
    EcranPlugInPool(QWidget* parent = nullptr);
    ~EcranPlugInPool();
};

#endif // ECRANPLUGINPOOL_H
