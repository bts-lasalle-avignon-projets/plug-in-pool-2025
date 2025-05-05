#include "pluginpool.h"
#include "communicationbluetooth.h"
#include "match.h"
#include "ecranpluginpool.h"

#include <QDebug>

PlugInPool::PlugInPool(QObject* parent) :
    QObject(parent), communicationBluetooth(new CommunicationBluetooth(this)),
    match(nullptr), ecranPlugInPool(qobject_cast<EcranPlugInPool*>(parent))

{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    match = new Match("Match test");

    connect(communicationBluetooth,
            &CommunicationBluetooth::connexionClient,
            this,
            &PlugInPool::gererConnexion);

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameRencontreRecue,
            this,
            &PlugInPool::configurerMatch);
}

PlugInPool::~PlugInPool()
{
    qDebug() << Q_FUNC_INFO << this;
    delete communicationBluetooth;
}

void PlugInPool::gererConnexion(bool etat)
{
    qDebug() << Q_FUNC_INFO << "etat" << etat;

    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();

    if(etat)
    {
        ecranAccueil->afficherEtatConnexion("Tablette connectée", etat);
    }
    else
    {
        ecranAccueil->afficherEtatConnexion("Tablette déconnectée", etat);
    }
}

void PlugInPool::configurerMatch(int     numeroTable,
                                 QString prenomJoueur1,
                                 QString prenomJoueur2,
                                 int     nbManches)
{
    qDebug() << Q_FUNC_INFO << "numeroTable" << numeroTable << "prenomJoueur1"
             << prenomJoueur1 << "prenomJoueur2" << prenomJoueur2 << "nbManches"
             << nbManches;

    EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
    match->setNumeroTable(numeroTable);
    match->enregistrerJoueurs(prenomJoueur1, prenomJoueur2);
    match->setNbManchesGagnantes(nbManches);
    ecranMatch->afficherInformationsMatch(numeroTable,
                                          prenomJoueur1,
                                          prenomJoueur2,
                                          nbManches);
    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    ecranAccueil->afficherEtatConfiguration("Configuration terminée");

    QTimer::singleShot(TEMPS_AVANT_LANCEMENT_RENCONTRE,
                       this,
                       &PlugInPool::changerEcranMatch);
}

void PlugInPool::changerEcranMatch()
{
    qDebug() << Q_FUNC_INFO;
    EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
    ecranPlugInPool->afficherEcranMatch();
    ecranMatch->demarrerChronometre();
}
