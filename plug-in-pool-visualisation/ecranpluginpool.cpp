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

    QFile file(":/pluginpool.css");
    if(file.open(QFile::ReadOnly | QFile::Text))
    {
        QTextStream in(&file);
        QString     style = in.readAll();
        this->setStyleSheet(style);
    }

    setWindowTitle(QString(NOM_APPLICATION) + QString(" v") +
                   QString(VERSION_APPLICATION));

    ecransInterface = new QStackedWidget(this);
    ecranAccueil    = new EcranAccueil(this);
    ecranMatch      = new EcranMatch(this);
    ecranFin        = new EcranFin(this);

    ecranAccueil->setObjectName("ecranAccueil");

    ecransInterface->addWidget(ecranAccueil);
    ecransInterface->addWidget(ecranMatch);
    ecransInterface->addWidget(ecranFin);

    interfacePlugInPool = new QVBoxLayout(this);

    interfacePlugInPool->addWidget(ecransInterface);

    setLayout(interfacePlugInPool);

#ifdef RASPBERRY_PI
    showFullScreen();
#endif

    afficherEcranAccueil();
}

EcranPlugInPool::~EcranPlugInPool()
{
    delete plugInPool;
    qDebug() << Q_FUNC_INFO << this;
}

void EcranPlugInPool::afficherEcranAccueil()
{
    ecransInterface->setCurrentWidget(ecranAccueil);
}

void EcranPlugInPool::afficherEcranMatch()
{
    ecransInterface->setCurrentWidget(ecranMatch);
}

void EcranPlugInPool::afficherEcranFin()
{
    ecransInterface->setCurrentWidget(ecranFin);
}
