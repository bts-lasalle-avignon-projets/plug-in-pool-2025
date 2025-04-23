#include "ecranmatch.h"
#include <QDebug>

EcranMatch::EcranMatch(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;
    affichageJoueurUn   = new QLabel("Joueur 1", ecran);
    affichageJoueurDeux = new QLabel("Joueur 2", ecran);
    chronometre         = new QTimer(ecran);
    labelChronometre    = new QLabel("00:00", ecran);

    affichageJoueurUn->setObjectName("affichageJoueurUn");
    affichageJoueurDeux->setObjectName("affichageJoueurDeux");
    chronometre->setObjectName("chronometre");
    labelChronometre->setObjectName("labelChronometre");

    QVBoxLayout* ecranMatch                    = new QVBoxLayout(ecran);
    QHBoxLayout* espaceJoueursEtCompteARebours = new QHBoxLayout();

    espaceJoueursEtCompteARebours->addWidget(affichageJoueurUn);
    espaceJoueursEtCompteARebours->addStretch();
    espaceJoueursEtCompteARebours->addWidget(labelChronometre);
    espaceJoueursEtCompteARebours->addStretch();
    espaceJoueursEtCompteARebours->addWidget(affichageJoueurDeux);

    ecranMatch->addLayout(espaceJoueursEtCompteARebours);
    ecranMatch->addStretch();
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
