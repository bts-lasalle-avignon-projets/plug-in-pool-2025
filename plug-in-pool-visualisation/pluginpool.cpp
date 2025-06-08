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

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameEmpochageRecue,
            this,
            &PlugInPool::empochage);

    connect(communicationBluetooth,
            &CommunicationBluetooth::tramePartieTermineeRecue,
            this,
            &PlugInPool::terminerPartie);

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameMatchTermineeRecue,
            this,
            &PlugInPool::terminerMatch);

    connect(communicationBluetooth,
            &CommunicationBluetooth::trameFauteRecue,
            this,
            &PlugInPool::afficherFaute);
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

    qDebug() << Q_FUNC_INFO << "Configurer match";

    EcranMatch*  ecranMatch  = ecranPlugInPool->getEcranMatch();
    EcranPartie* ecranPartie = ecranPlugInPool->getEcranPartie();
    match->enregistrerJoueurs(prenomJoueur1, prenomJoueur2);
    match->setNbManchesGagnantes(nbManches);
    match->reinitialiserTirsJoueurs();
    match->reinitialiserNbBillesBlanchesEmpocheesJoueurs();
    match->reinitialiserNbBillesEmpocheesJoueurs();

    ecranMatch->afficherInformationsMatch(nbManches,
                                          prenomJoueur1,
                                          prenomJoueur2);

    changerEcranMatch();

    connect(ecranMatch,
            &EcranMatch::compteAReboursDebutMatchTermine,
            this,
            [=]()
            {
                ecranPartie->afficherInformationsPartie(nbManches,
                                                        prenomJoueur1,
                                                        prenomJoueur2);
                changerEcranPartie();
            });
}

void PlugInPool::empochageCasse(int idPartie,
                                int idJoueur,
                                int couleurBille,
                                int idPoche)
{
    qDebug() << Q_FUNC_INFO << "idPartie" << idPartie << "idJoueur" << idJoueur
             << "couleurBille" << couleurBille << "idPoche" << idPoche;
    CouleurBille couleurBoule = static_cast<CouleurBille>(couleurBille);
    EcranPartie* ecranPartie  = ecranPlugInPool->getEcranPartie();
    QString      prenom       = match->getPrenomJoueur(idJoueur);
    switch(couleurBoule)
    {
        case ROUGE:
        {
            ecranPartie->retirerBoule(couleurBoule);
            ecranPartie->attribuerCouleurBille(idJoueur, couleurBoule);
            ecranPartie->incrementerCompteurPoche(couleurBoule, idPoche - 1);
            ecranPartie->afficherMessageAction(
              prenom + " a mis une bille rouge dans la poche " +
              QString::number(idPoche));
            break;
        }
        case JAUNE:
        {
            ecranPartie->retirerBoule(couleurBoule);
            ecranPartie->attribuerCouleurBille(idJoueur, couleurBoule);
            ecranPartie->incrementerCompteurPoche(couleurBoule, idPoche - 1);
            ecranPartie->afficherMessageAction(
              prenom + " a mis une bille jaune dans la poche " +
              QString::number(idPoche));
            break;
        }
        case BLANCHE:
        {
            match->augmenterCompteurBillesBlanchesEmpochees(idJoueur);
        }
    }
    match->attribuerCouleur(idJoueur, couleurBoule);
    match->augmenterCompteurEmpochagesReussis(idJoueur, couleurBoule);
    match->augmenterCompteurTirs(idJoueur);
    ecranPartie->demarrerCompteAReboursManche(TEMPS_COMPTE_A_REBOURS);
}

void PlugInPool::empochage(int idJoueur, int couleurBille, int idPoche)
{
    qDebug() << Q_FUNC_INFO << "idJoueur" << idJoueur << "couleurBille"
             << couleurBille << "idPoche" << idPoche;
    CouleurBille couleurBoule = static_cast<CouleurBille>(couleurBille);
    EcranPartie* ecranPartie  = ecranPlugInPool->getEcranPartie();
    QString      prenom       = match->getPrenomJoueur(idJoueur);

    switch(couleurBoule)
    {
        case ROUGE:
        {
            ecranPartie->afficherMessageAction(
              prenom + " a mis une bille rouge dans la poche " +
              QString::number(idPoche));
            ecranPartie->retirerBoule(couleurBoule);
            ecranPartie->incrementerCompteurPoche(couleurBoule, idPoche - 1);
            break;
        }
        case JAUNE:
        {
            ecranPartie->afficherMessageAction(
              prenom + " a mis une bille jaune dans la poche " +
              QString::number(idPoche));
            ecranPartie->retirerBoule(couleurBoule);
            ecranPartie->incrementerCompteurPoche(couleurBoule, idPoche - 1);
            break;
        }
        case BLANCHE:
        {
            match->augmenterCompteurBillesBlanchesEmpochees(idJoueur);
        }
    }
    match->augmenterCompteurEmpochagesReussis(idJoueur, couleurBoule);
    match->augmenterCompteurTirs(idJoueur);
    ecranPartie->demarrerCompteAReboursManche(TEMPS_COMPTE_A_REBOURS);
}

void PlugInPool::afficherFaute(int idJoueurFaute, QString faute)
{
    qDebug() << Q_FUNC_INFO << "idJoueurFaute" << idJoueurFaute << "faute"
             << faute;
    QString      prenom      = match->getPrenomJoueur(idJoueurFaute);
    EcranPartie* ecranPartie = ecranPlugInPool->getEcranPartie();
    ecranPartie->afficherMessageAction(prenom + " a mis la bille " + faute);
}

void PlugInPool::terminerPartie(int idPartie, int idJoueurGagnant)
{
    qDebug() << Q_FUNC_INFO << "idPartie" << idPartie << "idJoueurGagnant"
             << idJoueurGagnant;
    EcranFin* ecranFin      = ecranPlugInPool->getEcranFin();
    QString   prenomGagnant = match->getPrenomJoueur(idJoueurGagnant);

    QString prenomJoueurUn   = match->getPrenomJoueur(ID_JOUEUR_1);
    QString prenomJoueurDeux = match->getPrenomJoueur(ID_JOUEUR_2);

    QString tirsJoueurUn   = QString::number(match->getTirsJoueur(ID_JOUEUR_1));
    QString tirsJoueurDeux = QString::number(match->getTirsJoueur(ID_JOUEUR_2));

    QString billesBlanchesEmpocheesJoueurUn =
      QString::number(match->getBillesBlanchesEmpocheesJoueur(ID_JOUEUR_1));
    QString billesBlanchesEmpocheesJoueurDeux =
      QString::number(match->getBillesBlanchesEmpocheesJoueur(ID_JOUEUR_2));

    QString billesEmpocheesJoueurUn =
      QString::number(match->getBillesEmpocheesJoueur(ID_JOUEUR_1));
    QString billesEmpocheesJoueurDeux =
      QString::number(match->getBillesEmpocheesJoueur(ID_JOUEUR_2));

    ecranFin->afficherJoueurGagnant(prenomGagnant + " a gagné cette partie");
    ecranFin->afficherStatistiques(prenomJoueurUn,
                                   prenomJoueurDeux,
                                   tirsJoueurUn,
                                   tirsJoueurDeux,
                                   billesBlanchesEmpocheesJoueurUn,
                                   billesBlanchesEmpocheesJoueurDeux,
                                   billesEmpocheesJoueurUn,
                                   billesEmpocheesJoueurDeux);

    afficherDureePartie();

    changerEcranFin();
}

void PlugInPool::terminerMatch(int nbPartiesJoueurUn, int nbPartiesJoueurDeux)
{
    qDebug() << Q_FUNC_INFO << "nbPartiesJoueurUn" << nbPartiesJoueurUn
             << "nbPartiesJoueurDeux" << nbPartiesJoueurDeux;
    EcranFinMatch* ecranFinMatch = ecranPlugInPool->getEcranFinMatch();
    if(nbPartiesJoueurUn > nbPartiesJoueurDeux)
    {
        ecranFinMatch->afficherJoueurGagnant(match->getPrenomJoueur(0) +
                                             " a gagné ce Match");
    }
    else if(nbPartiesJoueurDeux > nbPartiesJoueurUn)
    {
        ecranFinMatch->afficherJoueurGagnant(match->getPrenomJoueur(1) +
                                             " a gagné ce Match");
    }
    ecranFinMatch->afficherScores(match->getPrenomJoueur(0) + " :      " +
                                  QString::number(nbPartiesJoueurUn) + "  -  " +
                                  QString::number(nbPartiesJoueurDeux) +
                                  "      : " + match->getPrenomJoueur(1));
    changerEcranFinMatch();
}

void PlugInPool::afficherDureePartie()
{
    qDebug() << Q_FUNC_INFO;
    EcranPartie* ecranPartie = ecranPlugInPool->getEcranPartie();
    EcranFin*    ecranFin    = ecranPlugInPool->getEcranFin();
    int          minutes     = ecranPartie->getSecondesEcoulees() / MINUTE;
    int          secondes    = ecranPartie->getSecondesEcoulees() % MINUTE;

    QString temps =
      QString("%1:%2")
        .arg(minutes, LARGEUR_MINUTE, BASE_DECIMALE, QChar('0'))
        .arg(secondes, LARGEUR_SECONDE, BASE_DECIMALE, QChar('0'));
    ecranFin->afficherDureePartie(temps);
}

void PlugInPool::changerEcranMatch()
{
    qDebug() << Q_FUNC_INFO;
    EcranMatch* ecranMatch = ecranPlugInPool->getEcranMatch();
    ecranPlugInPool->afficherEcranMatch();
    QTimer::singleShot(TEMPS_AVANT_COMPTE_A_REBOURS,
                       [ecranMatch]()
                       {
                           ecranMatch->demarrerCompteAReboursDebutMatch(
                             TEMPS_COMPTE_A_REBOURS_DEBUT_MATCH);
                       });
}
void PlugInPool::changerEcranPartie()
{
    qDebug() << Q_FUNC_INFO;
    EcranPartie* ecranPartie = ecranPlugInPool->getEcranPartie();
    ecranPlugInPool->afficherEcranPartie();
    ecranPartie->demarrerChronometre();
    ecranPartie->demarrerCompteAReboursManche(TEMPS_COMPTE_A_REBOURS);
}

void PlugInPool::changerEcranFin()
{
    ecranPlugInPool->afficherEcranFin();
}

void PlugInPool::changerEcranFinMatch()
{
    ecranPlugInPool->afficherEcranFinMatch();
}
