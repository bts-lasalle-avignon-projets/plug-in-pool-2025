#include "pluginpool.h"
#include "communicationbluetooth.h"
#include <QDebug>

PlugInPool::PlugInPool(QObject* parent) :
    QObject(parent), communicationBluetooth(new CommunicationBluetooth(this)),
    ecranPlugInPool(qobject_cast<EcranPlugInPool*>(parent))

{
    qDebug() << Q_FUNC_INFO << this << "parent" << parent;

    match = new Match("Match test");

    connect(communicationBluetooth,
            &CommunicationBluetooth::clientConnecte,
            this,
            &PlugInPool::bluetoothConnecte);

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameRencontreRecue,
            this,
            &PlugInPool::configurationRecu);
}

PlugInPool::~PlugInPool()
{
    qDebug() << Q_FUNC_INFO << this;
    delete communicationBluetooth;
}

void PlugInPool::bluetoothConnecte()
{
    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    ecranAccueil->getConnexionBluetoothLabel()->setText("Bluetooth Connecté");
    ecranAccueil->getConnexionBluetoothLabel()->setProperty("class",
                                                            "connecte");
    ecranAccueil->getConnexionBluetoothLabel()->style()->polish(
      ecranAccueil->getConnexionBluetoothLabel());
}

void PlugInPool::configurationRecu(int     numeroTable,
                                   QString prenomJoueur1,
                                   QString prenomJoueur2)
{
    qDebug() << "Joueurs reçus :" << prenomJoueur1 << "et" << prenomJoueur2;

    match->enregistrerJoueurs(prenomJoueur1, prenomJoueur2);

    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    EcranMatch*   ecranMatch   = ecranPlugInPool->getEcranMatch();
    QLabel*       configurationPartieStyle =
      ecranAccueil->getConfigurationPartieLabel();

    ecranMatch->getJoueurUnLabel()->setText(prenomJoueur1);
    ecranMatch->getJoueurDeuxLabel()->setText(prenomJoueur2);
    ecranMatch->getNumeroTableLabel()->setText("Table n°" +
                                               QString::number(numeroTable));
    configurationPartieStyle->setText("Configuration Terminée");

    mettreAJourStyleConfigurationPartie(configurationPartieStyle);

    QTimer::singleShot(TEMPS_AVANT_LANCEMENT_RENCONTRE,
                       this,
                       &PlugInPool::changerEcranMatch);
}

void PlugInPool::mettreAJourStyleConfigurationPartie(QLabel* configurationStyle)
{
    if(configurationStyle)
    {
        configurationStyle->setProperty("class", "configure");
        configurationStyle->style()->polish(configurationStyle);
    }
}

void PlugInPool::changerEcranMatch()
{
    EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
    qDebug() << "Passage à l'écran de match !";
    ecranPlugInPool->afficherEcranMatch();
    ecranMatch->demarrerChronometre();
}
