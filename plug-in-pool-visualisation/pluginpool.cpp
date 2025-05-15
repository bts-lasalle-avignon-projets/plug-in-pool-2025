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

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameCasseRecue,
            this,
            &PlugInPool::empochageCasse);
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

void PlugInPool::configurerMatch(int     nbManches,
                                 QString prenomJoueur1,
                                 QString prenomJoueur2)
{
    qDebug() << Q_FUNC_INFO << "prenomJoueur1" << prenomJoueur1
             << "prenomJoueur2" << prenomJoueur2 << "nbManches" << nbManches;

    EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
    match->enregistrerJoueurs(prenomJoueur1, prenomJoueur2);
    match->setNbManchesGagnantes(nbManches);
    ecranMatch->afficherInformationsMatch(nbManches,
                                          prenomJoueur1,
                                          prenomJoueur2);
    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    ecranAccueil->afficherEtatConfiguration("Configuration terminée");

    QTimer::singleShot(TEMPS_AVANT_LANCEMENT_RENCONTRE,
                       this,
                       &PlugInPool::changerEcranMatch);
}

void PlugInPool::empochageCasse(int idPartie,
                                int idJoueur,
                                int couleurBille,
                                int idPoche)
{
    qDebug() << Q_FUNC_INFO << "idPartie" << idPartie << "idJoueur" << idJoueur
             << "couleurBille" << couleurBille << "idPoche" << idPoche;
    CouleurBille couleurBoule = static_cast<CouleurBille>(couleurBille);
    EcranMatch*  ecranMatch   = ecranPlugInPool->getEcranMatch();
    switch(couleurBoule)
    {
        case ROUGE:
        {
            ecranMatch->afficherMessageAction("Le joueur " +
                                              QString::number(idJoueur + 1) +
                                              " a les boules Rouges");
            ecranMatch->retirerBoule(couleurBoule);
            ecranMatch->incrementerCompteurPoche(couleurBoule, idPoche - 1);
            break;
        }
        case JAUNE:
        {
            ecranMatch->afficherMessageAction("Le joueur " +
                                              QString::number(idJoueur + 1) +
                                              " a les boules Jaunes");
            ecranMatch->retirerBoule(couleurBoule);
            ecranMatch->incrementerCompteurPoche(couleurBoule, idPoche - 1);
            break;
        }
        case BLANCHE:
        {
            ecranMatch->afficherMessageAction("Faute : Joueur suivant");
            break;
        }
        case NOIR:
        {
            ecranMatch->afficherMessageAction("Faute : Partie Terminé");
            break;
        }
        case AUCUNE:
        {
            ecranMatch->afficherMessageAction("Faute : Joueur suivant");
            break;
        }
    }
}

void PlugInPool::changerEcranMatch()
{
    qDebug() << Q_FUNC_INFO;
    EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
    ecranPlugInPool->afficherEcranMatch();
    ecranMatch->demarrerChronometre();
}
