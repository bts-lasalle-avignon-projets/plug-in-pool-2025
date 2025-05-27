#include "ecranmatch.h"
#include <QDebug>

EcranMatch::EcranMatch(QWidget* parent) : QWidget(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;
    affichageJoueurUn                 = new QLabel("Joueur 1", ecran);
    affichageJoueurDeux               = new QLabel("Joueur 2", ecran);
    affichageNombreParties            = new QLabel("1 Partie Gagnante", ecran);
    affichageCompteAReboursDebutMatch = new QLabel(ecran);
    compteAReboursDebutMatch          = new QTimer(ecran);
    affichageFondCompteAReboursDebutMatch = new QLabel(ecran);

    affichageJoueurUn->setObjectName("affichageJoueurUnMatch");
    affichageJoueurDeux->setObjectName("affichageJoueurDeuxMatch");
    affichageNombreParties->setObjectName("affichageNombreParties");
    affichageCompteAReboursDebutMatch->setObjectName(
      "affichageCompteAReboursDebutMatch");
    affichageFondCompteAReboursDebutMatch->setObjectName(
      "affichageFondCompteAReboursDebutMatch");

    QVBoxLayout* ecranMatch = new QVBoxLayout(ecran);

    QHBoxLayout* espaceNombreParties = new QHBoxLayout(ecran);

    affichageFondCompteAReboursDebutMatch->setVisible(false);
    affichageCompteAReboursDebutMatch->setVisible(false);
    affichageFondCompteAReboursDebutMatch->clear();
    affichageCompteAReboursDebutMatch->clear();

    espaceNombreParties->addWidget(affichageNombreParties);

    affichageNombreParties->setAlignment(Qt::AlignCenter);

    ecranMatch->addStretch();
    ecranMatch->addLayout(espaceNombreParties);

    positionnerAffichageJoueurs();
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
    affichageNombreParties->setText(QString::number(nbManches) +
                                    " Partie(s) Gagnante(s)");
}

void EcranMatch::demarrerCompteAReboursDebutMatch(int dureeEnSecondes)
{
    compteAReboursDebutMatch->stop();
    compteAReboursDebutMatch->disconnect();
    secondesRestantes = dureeEnSecondes;

    affichageCompteAReboursDebutMatch->setVisible(true);
    affichageFondCompteAReboursDebutMatch->setVisible(true);

    affichageCompteAReboursDebutMatch->setText("5");

    connect(compteAReboursDebutMatch,
            &QTimer::timeout,
            this,
            [this]()
            {
                secondesRestantes--;
                if(secondesRestantes <= 0)
                {
                    compteAReboursDebutMatch->stop();
                    affichageCompteAReboursDebutMatch->clear();
                    affichageFondCompteAReboursDebutMatch->clear();
                    affichageCompteAReboursDebutMatch->setVisible(false);
                    affichageFondCompteAReboursDebutMatch->setVisible(false);

                    emit compteAReboursDebutMatchTermine();
                }
                else
                {
                    affichageCompteAReboursDebutMatch->setText(
                      QString::number(secondesRestantes));
                }
            });

    compteAReboursDebutMatch->start(TEMPS_INCREMENTATION);
}

void EcranMatch::positionnerAffichageJoueurs()
{
    affichageJoueurUn->setFixedSize(300, 100);
    affichageJoueurUn->move(300, 520);

    affichageJoueurDeux->setFixedSize(300, 100);
    affichageJoueurDeux->move(1410, 520);

    affichageCompteAReboursDebutMatch->setFixedSize(400, 400);
    affichageCompteAReboursDebutMatch->move(810, 410);

    affichageFondCompteAReboursDebutMatch->setFixedSize(480, 480);
    affichageFondCompteAReboursDebutMatch->move(700, 350);

    affichageFondCompteAReboursDebutMatch->lower();
    affichageCompteAReboursDebutMatch->raise();
}
