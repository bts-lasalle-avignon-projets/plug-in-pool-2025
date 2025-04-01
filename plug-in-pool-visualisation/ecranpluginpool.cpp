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

    ecransInterface             = new QStackedWidget(this);
    QWidget* widgetEcranAccueil = new QWidget();
    ecranAccueil                = new EcranAccueil(widgetEcranAccueil);
    QWidget* widgetEcranMatch   = new QWidget();
    ecranMatch                  = new EcranMatch(widgetEcranMatch);
    QWidget* widgetEcranFin     = new QWidget();
    ecranFin                    = new EcranFin(widgetEcranFin);

    ecransInterface->setObjectName("ecransInterface");
    widgetEcranAccueil->setObjectName("ecranAccueil");
    widgetEcranMatch->setObjectName("ecranMatch");
    widgetEcranFin->setObjectName("ecranFin");

    ecransInterface->addWidget(widgetEcranAccueil);
    ecransInterface->addWidget(widgetEcranMatch);
    ecransInterface->addWidget(widgetEcranFin);

    interfacePlugInPool = new QVBoxLayout(this);
    interfacePlugInPool->addWidget(ecransInterface);

#ifdef RASPBERRY_PI
    qDebug() << Q_FUNC_INFO << "RASPBERRY_PI";
    showFullScreen();
#else
    // setFixedSize(LARGEUR_ECRAN, HAUTEUR_ECRAN);
    showMaximized();
#endif

    afficherEcranAccueil();
    // afficherEcranMatch();
    // afficherEcranFin();
}

EcranPlugInPool::~EcranPlugInPool()
{
    delete plugInPool;
    qDebug() << Q_FUNC_INFO << this;
}

void EcranPlugInPool::afficherEcranAccueil()
{
    ecransInterface->setCurrentWidget(ecranAccueil->getEcran());
}

void EcranPlugInPool::afficherEcranMatch()
{
    ecransInterface->setCurrentWidget(ecranMatch->getEcran());
}

void EcranPlugInPool::afficherEcranFin()
{
    ecransInterface->setCurrentWidget(ecranFin->getEcran());
}
