/**
 * @file ecranpluginpool.cpp
 *
 * @brief Définition de la classe EcranPlugInPool
 * @author NAVARRO Mattéo
 * @version 1.0
 */

#include "ecranpluginpool.h"
#include "pluginpool.h"
#include <QDebug>

/**
 * @brief Constructeur de la classe EcranPlugInPool
 *
 * @fn EcranPlugInPool::EcranPlugInPool
 * @param parent L'adresse de l'objet parent, si nullptr EcranPlugInPool sera la
 * fenêtre principale de l'application
 */
EcranPlugInPool::EcranPlugInPool(QWidget* parent) :
    QWidget(parent), plugInPool(new PlugInPool(this))
{
    qDebug() << Q_FUNC_INFO << this;

    setWindowTitle(QString(NOM_APPLICATION) + QString(" v") +
                   QString(VERSION_APPLICATION));

#ifdef RASPBERRY_PI
    showFullScreen();
#endif
}

EcranPlugInPool::~EcranPlugInPool()
{
    delete plugInPool;
    qDebug() << Q_FUNC_INFO << this;
}
