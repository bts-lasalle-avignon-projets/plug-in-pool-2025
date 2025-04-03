#include "ecranmatch.h"
#include <QDebug>

EcranMatch::EcranMatch(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;
    affichageJoueurUn   = new QLabel("Joueur 1", ecran);
    affichageJoueurDeux = new QLabel("Joueur 2", ecran);

    affichageJoueurUn->setObjectName("affichageJoueurUn");
    affichageJoueurDeux->setObjectName("affichageJoueurDeux");

    QVBoxLayout* ecranMatch                    = new QVBoxLayout(ecran);
    QHBoxLayout* espaceJoueursEtCompteARebours = new QHBoxLayout();

    espaceJoueursEtCompteARebours->addWidget(affichageJoueurUn);
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
