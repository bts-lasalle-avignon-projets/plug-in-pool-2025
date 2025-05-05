#include "ecranmatch.h"
#include <QDebug>

EcranMatch::EcranMatch(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageJoueurUn         = new QLabel("Joueur 1", ecran);
    affichageJoueurDeux       = new QLabel("Joueur 2", ecran);
    chronometre               = new QTimer(ecran);
    compteAReboursManche      = new QTimer(ecran);
    labelChronometre          = new QLabel("00:00", ecran);
    labelCompteAReboursManche = new QLabel("01:30", ecran);
    affichageNumeroTable      = new QLabel("Table n°", ecran);
    affichageNomJeu           = new QLabel("PlugInPool", ecran);

    affichageJoueurUn->setObjectName("affichageJoueurUn");
    affichageJoueurDeux->setObjectName("affichageJoueurDeux");
    chronometre->setObjectName("chronometre");
    labelChronometre->setObjectName("labelChronometre");
    compteAReboursManche->setObjectName("compteAReboursManche");
    labelCompteAReboursManche->setObjectName("labelCompteAReboursManche");
    affichageNumeroTable->setObjectName("affichageNumeroTable");
    affichageNomJeu->setObjectName("affichageNomJeu");

    QVBoxLayout* ecranMatch                     = new QVBoxLayout(ecran);
    QHBoxLayout* espaceJoueursEtCompteARebours  = new QHBoxLayout();
    QHBoxLayout* espaceBoules                   = new QHBoxLayout();
    espaceTableBillard                          = new QGridLayout();
    QHBoxLayout* espaceNumeroTableEtChronometre = new QHBoxLayout();

    espaceBoulesRouges = new QHBoxLayout();
    espaceBoulesJaunes = new QHBoxLayout();
    espaceBouleBlanche = new QHBoxLayout();
    espaceBouleNoir    = new QHBoxLayout();

    espaceJoueursEtCompteARebours->addWidget(affichageJoueurUn);
    espaceJoueursEtCompteARebours->addStretch();
    espaceJoueursEtCompteARebours->addWidget(labelCompteAReboursManche);
    espaceJoueursEtCompteARebours->addStretch();
    espaceJoueursEtCompteARebours->addWidget(affichageJoueurDeux);

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

    ecranMatch->addLayout(espaceJoueursEtCompteARebours);
    ecranMatch->addSpacing(100);
    ecranMatch->addLayout(espaceBoules);
    ecranMatch->addStretch();
    ecranMatch->addLayout(espaceTableBillard);
    ecranMatch->addStretch();
    ecranMatch->addLayout(espaceNumeroTableEtChronometre);

    genererBoules();
    initialiserPochesTable();
}

EcranMatch::~EcranMatch()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranMatch::getEcran() const
{
    return ecran;
}

void EcranMatch::afficherInformationsMatch(int     numeroTable,
                                           QString joueur1,
                                           QString joueur2,
                                           int     nbManches)
{
    affichageJoueurUn->setText(joueur1);
    affichageJoueurDeux->setText(joueur2);
    affichageNumeroTable->setText("Table n°" + QString::number(numeroTable));
}

void EcranMatch::demarrerChronometre()
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

void EcranMatch::genererBoules()
{
    for(int i = 0; i < NB_BOULES_ROUGES; ++i)
    {
        QLabel* bouleRouge = new QLabel(ecran);
        bouleRouge->setObjectName("bouleRouge");
        espaceBoulesRouges->addWidget(bouleRouge);
        boulesRouges.append(bouleRouge);
    }

    for(int i = 0; i < NB_BOULES_JAUNES; ++i)
    {
        QLabel* bouleJaune = new QLabel(ecran);
        bouleJaune->setObjectName("bouleJaune");
        espaceBoulesJaunes->addWidget(bouleJaune);
        boulesJaunes.append(bouleJaune);
    }

    QLabel* bouleBlanche = new QLabel(ecran);
    bouleBlanche->setObjectName("bouleBlanche");
    espaceBouleBlanche->addWidget(bouleBlanche);

    QLabel* bouleNoir = new QLabel(ecran);
    bouleNoir->setObjectName("bouleNoir");
    espaceBouleNoir->addWidget(bouleNoir);
}

void EcranMatch::initialiserPochesTable()
{
    for(int i = 0; i < NB_POCHES; ++i)
    {
        bouleRougeImagePoche[i]      = new QLabel(ecran);
        bouleJauneImagePoche[i]      = new QLabel(ecran);
        compteurBoulesRougesPoche[i] = new QLabel("0", ecran);
        compteurBoulesJaunesPoche[i] = new QLabel("0", ecran);

        bouleRougeImagePoche[i]->setObjectName(
          QString("bouleRougeImagePoche%1").arg(i));
        compteurBoulesRougesPoche[i]->setObjectName(
          QString("compteurBoulesRougesPoche%1").arg(i));
        bouleJauneImagePoche[i]->setObjectName(
          QString("bouleJauneImagePoche%1").arg(i));
        compteurBoulesJaunesPoche[i]->setObjectName(
          QString("compteurBoulesJaunesPoche%1").arg(i));

        QWidget* poche = new QWidget(ecran);
        poche->setObjectName("poche");
        QVBoxLayout* contenuPoche = new QVBoxLayout(poche);

        contenuPoche->addWidget(bouleRougeImagePoche[i]);
        contenuPoche->addWidget(compteurBoulesRougesPoche[i]);
        contenuPoche->addWidget(bouleJauneImagePoche[i]);
        contenuPoche->addWidget(compteurBoulesJaunesPoche[i]);

        espaceTableBillard->addWidget(poche,
                                      lignesPoches[i],
                                      colonnesPoches[i]);
    }
}
