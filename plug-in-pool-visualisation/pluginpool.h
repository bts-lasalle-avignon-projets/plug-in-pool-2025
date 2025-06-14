#ifndef PLUGINPOOL_H
#define PLUGINPOOL_H

#include <QObject>
#include <QTimer>

#define TEMPS_AVANT_LANCEMENT_RENCONTRE    5000
#define TEMPS_AVANT_COMPTE_A_REBOURS       5000
#define TEMPS_COMPTE_A_REBOURS             90
#define TEMPS_COMPTE_A_REBOURS_DEBUT_MATCH 5
#define RASPBERRY_PI

#define ID_JOUEUR_1 0
#define ID_JOUEUR_2 1

class CommunicationBluetooth;
class Match;
class EcranPlugInPool;

class PlugInPool : public QObject
{
    Q_OBJECT
  private:
    CommunicationBluetooth* communicationBluetooth;
    Match*                  match;
    EcranPlugInPool*        ecranPlugInPool;

  public:
    PlugInPool(QObject* parent = nullptr);
    virtual ~PlugInPool();

  private slots:
    void gererConnexion(bool etat);
    void configurerMatch(int     nbManches,
                         QString prenomJoueur1,
                         QString prenomJoueur2);
    void empochageCasse(int idPartie,
                        int idJoueur,
                        int couleurBille,
                        int idPoche);
    void empochage(int idJoueur, int couleurBille, int idPoche);
    void terminerPartie(int idPartie, int idJoueurGagnant);
    void terminerMatch(int nbPartiesJoueurUn, int nbPartiesJoueurDeux);
    void afficherFaute(int idJoueurFaute, QString faute);
    void afficherDureePartie();
    void changerEcranMatch();
    void changerEcranPartie();
    void changerEcranFin();
    void changerEcranFinMatch();
};

#endif // PLUGINPOOL_H
