#include "ecranmatch.h"
#include <QDebug>

EcranMatch::EcranMatch(QWidget* parent) : QWidget(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;
    affichageJoueurUn      = new QLabel("Joueur 1", ecran);
    affichageJoueurDeux    = new QLabel("Joueur 2", ecran);
    affichageNombreParties = new QLabel("1 Partie Gagnante", ecran);

    affichageJoueurUn->setObjectName("affichageJoueurUnMatch");
    affichageJoueurDeux->setObjectName("affichageJoueurDeuxMatch");
    affichageNombreParties->setObjectName("affichageNombreParties");

    QVBoxLayout* ecranMatch = new QVBoxLayout(ecran);

    QHBoxLayout* espaceJoueurs       = new QHBoxLayout(ecran);
    QHBoxLayout* espaceJoueurUn      = new QHBoxLayout(ecran);
    QHBoxLayout* espaceJoueurDeux    = new QHBoxLayout(ecran);
    QHBoxLayout* espaceNombreParties = new QHBoxLayout(ecran);

    espaceJoueurs->addLayout(espaceJoueurUn);
    espaceJoueurs->addLayout(espaceJoueurDeux);

    espaceJoueurUn->addStretch();
    espaceJoueurUn->addWidget(affichageJoueurUn);
    espaceJoueurUn->addStretch();

    espaceJoueurDeux->addStretch();
    espaceJoueurDeux->addWidget(affichageJoueurDeux);
    espaceJoueurDeux->addStretch();

    espaceNombreParties->addWidget(affichageNombreParties);

    ecranMatch->addLayout(espaceJoueurs);
    ecranMatch->addStretch();
    ecranMatch->addLayout(espaceNombreParties);
}

EcranMatch::~EcranMatch()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranMatch::getEcran() const
{
    return ecran;
}

void EcranMatch::afficherInformationsMatch(int     nbManches,
                                           QString joueur1,
                                           QString joueur2)
{
    affichageJoueurUn->setText(joueur1);
    affichageJoueurDeux->setText(joueur2);
    affichageNombreParties->setText(nbManches + " Manche(s) Gagnante(s)");
}
