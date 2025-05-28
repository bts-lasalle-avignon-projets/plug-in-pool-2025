#include "ecranpartie.h"
#include <QDebug>

EcranPartie::EcranPartie(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageJoueurUn                  = new QLabel("Joueur 1", ecran);
    affichageJoueurDeux                = new QLabel("Joueur 2", ecran);
    chronometre                        = new QTimer(ecran);
    compteAReboursManche               = new QTimer(ecran);
    labelChronometre                   = new QLabel("00:00", ecran);
    labelCompteAReboursManche          = new QLabel("01:30", ecran);
    affichageNumeroTable               = new QLabel("Table n° 1", ecran);
    affichageNomJeu                    = new QLabel("PlugInPool", ecran);
    affichageMessage                   = new QLabel(ecran);
    affichageCouleurAttribueJoueurUn   = new QLabel(ecran);
    affichageCouleurAttribueJoueurDeux = new QLabel(ecran);

    affichageJoueurUn->setObjectName("affichageJoueurUn");
    affichageJoueurDeux->setObjectName("affichageJoueurDeux");
    chronometre->setObjectName("chronometre");
    labelChronometre->setObjectName("labelChronometre");
    compteAReboursManche->setObjectName("compteAReboursManche");
    labelCompteAReboursManche->setObjectName("labelCompteAReboursManche");
    affichageNumeroTable->setObjectName("affichageNumeroTable");
    affichageNomJeu->setObjectName("affichageNomJeu");
    affichageMessage->setObjectName("affichageMessage");
    affichageCouleurAttribueJoueurUn->setObjectName(
      "affichageCouleurAttribueJoueurUn");
    affichageCouleurAttribueJoueurDeux->setObjectName(
      "affichageCouleurAttribueJoueurDeux");

    affichageMessage->setFixedSize(700, 100);
    affichageMessage->move(650, 520);
    affichageMessage->setAlignment(Qt::AlignCenter);

    QVBoxLayout* ecranPartie                    = new QVBoxLayout(ecran);
    QHBoxLayout* espaceJoueursEtCompteARebours  = new QHBoxLayout();
    QHBoxLayout* espaceBoules                   = new QHBoxLayout();
    QHBoxLayout* espaceNumeroTableEtChronometre = new QHBoxLayout();

    espaceBoulesRouges = new QHBoxLayout();
    espaceBoulesJaunes = new QHBoxLayout();
    espaceBouleBlanche = new QHBoxLayout();
    espaceBouleNoir    = new QHBoxLayout();

    espaceJoueursEtCompteARebours->addWidget(affichageJoueurUn);
    espaceJoueursEtCompteARebours->addSpacing(100);
    espaceJoueursEtCompteARebours->addWidget(affichageCouleurAttribueJoueurUn);
    espaceJoueursEtCompteARebours->addStretch();
    espaceJoueursEtCompteARebours->addWidget(labelCompteAReboursManche);
    espaceJoueursEtCompteARebours->addStretch();
    espaceJoueursEtCompteARebours->addWidget(
      affichageCouleurAttribueJoueurDeux);
    espaceJoueursEtCompteARebours->addSpacing(100);
    espaceJoueursEtCompteARebours->addWidget(affichageJoueurDeux);

    // espaceBoulesRouges->addStretch();
    // espaceBoulesJaunes->addStretch();

    espaceBoules->addStretch();
    espaceBoules->addLayout(espaceBoulesRouges);
    espaceBoules->addSpacing(20);
    espaceBoules->addLayout(espaceBouleBlanche);
    espaceBoules->addSpacing(20);
    espaceBoules->addLayout(espaceBouleNoir);
    espaceBoules->addSpacing(20);
    espaceBoules->addLayout(espaceBoulesJaunes);
    espaceBoules->addStretch();

    espaceNumeroTableEtChronometre->addWidget(affichageNumeroTable);
    espaceNumeroTableEtChronometre->addStretch();
    espaceNumeroTableEtChronometre->addWidget(labelChronometre);
    espaceNumeroTableEtChronometre->addStretch();
    espaceNumeroTableEtChronometre->addWidget(affichageNomJeu);

    ecranPartie->addLayout(espaceJoueursEtCompteARebours);
    ecranPartie->addSpacing(80);
    ecranPartie->addLayout(espaceBoules);
    ecranPartie->addStretch();
    ecranPartie->addStretch();
    ecranPartie->addLayout(espaceNumeroTableEtChronometre);

    genererBoules();
    initialiserPochesTable();
}

EcranPartie::~EcranPartie()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranPartie::getEcran() const
{
    return ecran;
}

void EcranPartie::afficherInformationsPartie(int     nbManches,
                                             QString joueur1,
                                             QString joueur2)
{
    affichageJoueurUn->setText(joueur1);
    affichageJoueurDeux->setText(joueur2);
}

void EcranPartie::demarrerChronometre()
{
    secondesEcoulees = 0;

    connect(chronometre,
            &QTimer::timeout,
            this,
            [=]()
            {
                secondesEcoulees++;

                int minutes  = secondesEcoulees / MINUTE;
                int secondes = secondesEcoulees % MINUTE;

                QString temps =
                  QString("%1:%2")
                    .arg(minutes, LARGEUR_MINUTE, BASE_DECIMALE, QChar('0'))
                    .arg(secondes, LARGEUR_SECONDE, BASE_DECIMALE, QChar('0'));
                labelChronometre->setText(temps);
            });

    chronometre->start(TEMPS_INCREMENTATION);
}

void EcranPartie::demarrerCompteAReboursManche(int dureeEnSecondes)
{
    compteAReboursManche->stop();
    compteAReboursManche->disconnect();
    secondesRestantes = dureeEnSecondes;

    labelCompteAReboursManche->setText("00:00");

    connect(
      compteAReboursManche,
      &QTimer::timeout,
      this,
      [=]()
      {
          if(secondesRestantes <= 0)
          {
              compteAReboursManche->stop();
              labelCompteAReboursManche->setText("00:00");
              afficherMessageAction("Temps écoulé ! Au tour du joueur suivant");
          }
          else
          {
              int minutes  = secondesRestantes / MINUTE;
              int secondes = secondesRestantes % MINUTE;

              QString temps =
                QString("%1:%2")
                  .arg(minutes, LARGEUR_MINUTE, BASE_DECIMALE, QChar('0'))
                  .arg(secondes, LARGEUR_SECONDE, BASE_DECIMALE, QChar('0'));

              labelCompteAReboursManche->setText(temps);
              secondesRestantes--;
          }
      });

    compteAReboursManche->start(TEMPS_INCREMENTATION);
}

void EcranPartie::genererBoules()
{
    for(int i = 0; i < NB_BOULES_ROUGES; ++i)
    {
        QLabel* bouleRouge = new QLabel(ecran);
        bouleRouge->setObjectName("bouleRouge");
        espaceBoulesRouges->addWidget(bouleRouge);
        boulesRouges.append(bouleRouge);
    }

    espaceBoulesRouges->insertStretch(0, 1);

    for(int i = 0; i < NB_BOULES_JAUNES; ++i)
    {
        QLabel* bouleJaune = new QLabel(ecran);
        bouleJaune->setObjectName("bouleJaune");
        espaceBoulesJaunes->addWidget(bouleJaune);
        boulesJaunes.append(bouleJaune);
    }

    espaceBoulesJaunes->addStretch();

    QLabel* bouleBlanche = new QLabel(ecran);
    bouleBlanche->setObjectName("bouleBlanche");
    espaceBouleBlanche->addWidget(bouleBlanche);

    QLabel* bouleNoir = new QLabel(ecran);
    bouleNoir->setObjectName("bouleNoir");
    espaceBouleNoir->addWidget(bouleNoir);
}

void EcranPartie::initialiserPochesTable()
{
    const QPoint positionCompteurRouge[NB_POCHES] = {
        { 1525, 305 }, { 1448, 890 }, { 959, 230 },
        { 832, 890 },  { 340, 230 },  { 266, 815 }
    };

    const QPoint positionCompteurJaune[NB_POCHES] = {
        { 1450, 230 }, { 1525, 815 }, { 830, 230 },
        { 960, 890 },  { 262, 305 },  { 340, 890 }
    };

    for(int i = 0; i < NB_POCHES; ++i)
    {
        compteurBoulesRougesPoche[i] = new QLabel("0", ecran);
        compteurBoulesRougesPoche[i]->setObjectName(
          QString("compteurBoulesRougesPoche%1").arg(i));
        compteurBoulesRougesPoche[i]->setAlignment(Qt::AlignCenter);
        compteurBoulesRougesPoche[i]->move(positionCompteurRouge[i]);

        // Création des compteurs jaunes
        compteurBoulesJaunesPoche[i] = new QLabel("0", ecran);
        compteurBoulesJaunesPoche[i]->setObjectName(
          QString("compteurBoulesJaunesPoche%1").arg(i));
        compteurBoulesJaunesPoche[i]->setAlignment(Qt::AlignCenter);
        compteurBoulesJaunesPoche[i]->move(positionCompteurJaune[i]);
    }
}

void EcranPartie::retirerBoule(CouleurBille couleur)
{
    switch(couleur)
    {
        case ROUGE:
            if(!boulesRouges.isEmpty())
            {
                QLabel* boule = boulesRouges.takeFirst();
                delete boule;
            }
            break;

        case JAUNE:
            if(!boulesJaunes.isEmpty())
            {
                QLabel* boule = boulesJaunes.takeLast();
                delete boule;
            }
            break;
        default:
            qWarning() << Q_FUNC_INFO << "Couleur inconnue :" << couleur;
            break;
    }
}

void EcranPartie::incrementerCompteurPoche(CouleurBille couleur, int idPoche)
{
    if(idPoche < 0 || idPoche >= NB_POCHES)
        return;

    QLabel* compteur = nullptr;

    switch(couleur)
    {
        case ROUGE:
            compteur = compteurBoulesRougesPoche[idPoche];
            break;
        case JAUNE:
            compteur = compteurBoulesJaunesPoche[idPoche];
            break;
        default:
            return;
    }

    if(compteur)
    {
        int valeur = compteur->text().toInt();
        compteur->setText(QString::number(valeur + 1));
    }
}

void EcranPartie::afficherMessageAction(QString message)
{
    affichageMessage->setText(message);
}

void EcranPartie::attribuerCouleurBille(int idJoueur, int couleurBille)
{
    if(idJoueur == 0)
    {
        if(couleurBille == ROUGE)
        {
            affichageCouleurAttribueJoueurUn->setProperty("class",
                                                          "attribuerRouge");
            affichageCouleurAttribueJoueurDeux->setProperty("class",
                                                            "attribuerJaune");
        }
        else if(couleurBille == JAUNE)
        {
            affichageCouleurAttribueJoueurUn->setProperty("class",
                                                          "attribuerJaune");
            affichageCouleurAttribueJoueurDeux->setProperty("class",
                                                            "attribuerRouge");
        }
        else
        {
        }
    }
    else if(idJoueur == 1)
    {
        if(couleurBille == ROUGE)
        {
            affichageCouleurAttribueJoueurDeux->setProperty("class",
                                                            "attribuerRouge");
            affichageCouleurAttribueJoueurUn->setProperty("class",
                                                          "attribuerJaune");
        }
        else if(couleurBille == JAUNE)
        {
            affichageCouleurAttribueJoueurDeux->setProperty("class",
                                                            "attribuerJaune");
            affichageCouleurAttribueJoueurUn->setProperty("class",
                                                          "attribuerRouge");
        }
        else
        {
        }
    }
    else
    {
    }
    affichageCouleurAttribueJoueurUn->style()->polish(
      affichageCouleurAttribueJoueurUn);
    affichageCouleurAttribueJoueurDeux->style()->polish(
      affichageCouleurAttribueJoueurDeux);
}
