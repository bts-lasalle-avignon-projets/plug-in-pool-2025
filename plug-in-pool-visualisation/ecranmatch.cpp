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
    QHBoxLayout* espaceTableBillard             = new QHBoxLayout();
    QHBoxLayout* espaceNumeroTableEtChronometre = new QHBoxLayout();
    espaceBoulesRouges                          = new QHBoxLayout();
    espaceBoulesJaunes                          = new QHBoxLayout();
    espaceBouleBlanche                          = new QHBoxLayout();
    espaceBouleNoir                             = new QHBoxLayout();

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
}

EcranMatch::~EcranMatch()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranMatch::getEcran() const
{
    return ecran;
}

QLabel* EcranMatch::getJoueurUnLabel() const
{
    qDebug() << "Adresse QLabel (affichageJoueurUn): " << affichageJoueurUn;
    return affichageJoueurUn;
}

QLabel* EcranMatch::getJoueurDeuxLabel() const
{
    qDebug() << "Adresse QLabel (affichageJoueurDeux): " << affichageJoueurDeux;
    return affichageJoueurDeux;
}

QLabel* EcranMatch::getNumeroTableLabel() const
{
    return affichageNumeroTable;
}

void EcranMatch::demarrerChronometre()
{
    secondesEcoulees = 0;

    connect(chronometre, &QTimer::timeout, this, [=]() {
        secondesEcoulees++;

        int minutes  = secondesEcoulees / 60;
        int secondes = secondesEcoulees % 60;

        QString temps = QString("%1:%2")
                          .arg(minutes, 2, 10, QChar('0'))
                          .arg(secondes, 2, 10, QChar('0'));
        labelChronometre->setText(temps);
    });

    chronometre->start(1000);
}

void EcranMatch::genererBoules()
{
    for(int i = 0; i < 7; ++i)
    {
        QLabel* bouleRouge = new QLabel(ecran);

        bouleRouge->setObjectName("bouleRouge");
        bouleRouge->move(20 + i * 35, 20);
        bouleRouge->show();
        espaceBoulesRouges->addWidget(bouleRouge);
        boulesRouges.append(bouleRouge);
    }

    for(int i = 0; i < 7; ++i)
    {
        QLabel* bouleJaune = new QLabel(ecran);

        bouleJaune->setObjectName("bouleJaune");
        bouleJaune->move(20 + i * 35, 20);
        bouleJaune->show();
        espaceBoulesJaunes->addWidget(bouleJaune);
        boulesJaunes.append(bouleJaune);
    }

    QLabel* bouleBlanche = new QLabel(ecran);
    bouleBlanche->setObjectName("bouleBlanche");
    bouleBlanche->show();
    espaceBouleBlanche->addWidget(bouleBlanche);

    QLabel* bouleNoir = new QLabel(ecran);
    bouleNoir->setObjectName("bouleNoir");
    bouleNoir->show();
    espaceBouleNoir->addWidget(bouleNoir);
}
