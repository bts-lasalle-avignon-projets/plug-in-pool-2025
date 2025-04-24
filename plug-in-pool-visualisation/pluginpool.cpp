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
    if(ecranAccueil && ecranAccueil->getConnexionBluetoothLabel())
    {
        ecranAccueil->getConnexionBluetoothLabel()->setText(
          "Bluetooth Connecté");
        ecranAccueil->getConnexionBluetoothLabel()->setProperty("class",
                                                                "connecte");
        ecranAccueil->getConnexionBluetoothLabel()->style()->unpolish(
          ecranAccueil->getConnexionBluetoothLabel());
        ecranAccueil->getConnexionBluetoothLabel()->style()->polish(
          ecranAccueil->getConnexionBluetoothLabel());
        ecranAccueil->getConnexionBluetoothLabel()->update();
    }
}

void PlugInPool::configurationRecu(int     numeroTable,
                                   QString joueur1,
                                   QString joueur2)
{
    qDebug() << "Joueurs reçus :" << joueur1 << "et" << joueur2;

    match->enregistrerJoueurs(joueur1, joueur2);

    EcranAccueil* ecranAccueil = ecranPlugInPool->getEcranAccueil();
    EcranMatch*   ecranMatch   = ecranPlugInPool->getEcranMatch();
    QLabel*       configurationPartieStyle =
      ecranAccueil->getConfigurationPartieLabel();

    if(ecranAccueil && configurationPartieStyle)
    {
        ecranMatch->getJoueurUnLabel()->setText(joueur1);
        ecranMatch->getJoueurDeuxLabel()->setText(joueur2);
        ecranMatch->getNumeroTableLabel()->setText(
          "Table n°" + QString::number(numeroTable));

        configurationPartieStyle->setText("Configuration Terminée");

        mettreAJourStyleConfigurationPartie(configurationPartieStyle);

        QTimer::singleShot(TEMPS_AVANT_LANCEMENT_RENCONTRE,
                           this,
                           &PlugInPool::changerEcranMatch);
    }
}

void PlugInPool::mettreAJourStyleConfigurationPartie(QLabel* configurationStyle)
{
    if(configurationStyle)
    {
        configurationStyle->setProperty("class", "configure");
        configurationStyle->style()->unpolish(configurationStyle);
        configurationStyle->style()->polish(configurationStyle);
        configurationStyle->update();
    }
}

void PlugInPool::changerEcranMatch()
{
    if(ecranPlugInPool)
    {
        EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
        qDebug() << "Passage à l'écran de match !";
        ecranPlugInPool->afficherEcranMatch();
        ecranMatch->demarrerChronometre();
    }
}
