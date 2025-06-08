#include "ecranfin.h"
#include <QDebug>

EcranFin::EcranFin(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageVersion       = new QLabel("v1.1", ecran);
    affichageJoueurGagnant = new QLabel("Aucun joueur gagnant", ecran);

    affichageJoueurUn                        = new QLabel("Joueur 1", ecran);
    affichageTirsJoueurUn                    = new QLabel(ecran);
    affichageBillesBlanchesEmpocheesJoueurUn = new QLabel(ecran);
    affichageEmpochagesReussisJoueurUn       = new QLabel(ecran);

    affichageJoueurDeux                        = new QLabel("Joueur 2", ecran);
    affichageTirsJoueurDeux                    = new QLabel(ecran);
    affichageBillesBlanchesEmpocheesJoueurDeux = new QLabel(ecran);
    affichageEmpochagesReussisJoueurDeux       = new QLabel(ecran);

    affichageDureePartie = new QLabel("00:00", ecran);

    affichageJoueurUn->setAlignment(Qt::AlignCenter);
    affichageJoueurDeux->setAlignment(Qt::AlignCenter);
    affichageTirsJoueurUn->setAlignment(Qt::AlignCenter);
    affichageTirsJoueurDeux->setAlignment(Qt::AlignCenter);
    affichageBillesBlanchesEmpocheesJoueurUn->setAlignment(Qt::AlignCenter);
    affichageBillesBlanchesEmpocheesJoueurDeux->setAlignment(Qt::AlignCenter);
    affichageEmpochagesReussisJoueurUn->setAlignment(Qt::AlignCenter);
    affichageEmpochagesReussisJoueurDeux->setAlignment(Qt::AlignCenter);
    affichageDureePartie->setAlignment(Qt::AlignCenter);

    affichageVersion->setObjectName("affichageVersion");
    affichageJoueurGagnant->setObjectName("affichageJoueurGagnant");

    affichageJoueurUn->setObjectName("affichageJoueurUnFinPartie");
    affichageTirsJoueurUn->setObjectName("affichageTirsJoueurUn");
    affichageBillesBlanchesEmpocheesJoueurUn->setObjectName(
      "affichageBillesBlanchesEmpocheesJoueurUn");
    affichageEmpochagesReussisJoueurUn->setObjectName(
      "affichageEmpochagesReussisJoueurUn");

    affichageJoueurDeux->setObjectName("affichageJoueurDeuxFinPartie");
    affichageTirsJoueurDeux->setObjectName("affichageTirsJoueurUn");
    affichageBillesBlanchesEmpocheesJoueurDeux->setObjectName(
      "affichageBillesBlanchesEmpocheesJoueurUn");
    affichageEmpochagesReussisJoueurDeux->setObjectName(
      "affichageEmpochagesReussisJoueurUn");

    affichageDureePartie->setObjectName("affichageDureePartie");

    QVBoxLayout* ecranFin                     = new QVBoxLayout(ecran);
    QHBoxLayout* espaceVersion                = new QHBoxLayout();
    QHBoxLayout* espaceJoueurGagnant          = new QHBoxLayout();
    QHBoxLayout* espaceStatistiques           = new QHBoxLayout();
    QVBoxLayout* espaceStatistiquesJoueurUn   = new QVBoxLayout();
    QVBoxLayout* espaceStatistiquesJoueurDeux = new QVBoxLayout();
    QHBoxLayout* espaceDureePartie            = new QHBoxLayout();

    espaceVersion->addStretch();
    espaceVersion->addWidget(affichageVersion);
    espaceVersion->addStretch();

    espaceJoueurGagnant->addStretch();
    espaceJoueurGagnant->addWidget(affichageJoueurGagnant);
    espaceJoueurGagnant->addStretch();

    espaceStatistiquesJoueurUn->addWidget(affichageJoueurUn);
    espaceStatistiquesJoueurUn->addWidget(affichageTirsJoueurUn);
    espaceStatistiquesJoueurUn->addWidget(
      affichageBillesBlanchesEmpocheesJoueurUn);
    espaceStatistiquesJoueurUn->addWidget(affichageEmpochagesReussisJoueurUn);

    espaceStatistiquesJoueurDeux->addWidget(affichageJoueurDeux);
    espaceStatistiquesJoueurDeux->addWidget(affichageTirsJoueurDeux);
    espaceStatistiquesJoueurDeux->addWidget(
      affichageBillesBlanchesEmpocheesJoueurDeux);
    espaceStatistiquesJoueurDeux->addWidget(
      affichageEmpochagesReussisJoueurDeux);

    espaceStatistiques->addStretch();
    espaceStatistiques->addLayout(espaceStatistiquesJoueurUn);
    espaceStatistiques->addSpacing(80);
    espaceStatistiques->addLayout(espaceStatistiquesJoueurDeux);
    espaceStatistiques->addStretch();

    espaceDureePartie->addStretch();
    espaceDureePartie->addWidget(affichageDureePartie);
    espaceDureePartie->addStretch();

    ecranFin->addStretch();
    ecranFin->addSpacing(90);
    ecranFin->addLayout(espaceVersion);
    ecranFin->addSpacing(480);
    ecranFin->addLayout(espaceJoueurGagnant);
    ecranFin->addStretch();
    ecranFin->addLayout(espaceStatistiques);
    ecranFin->addLayout(espaceDureePartie);
    ecranFin->addSpacing(30);
}

EcranFin::~EcranFin()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranFin::getEcran() const
{
    return ecran;
}

void EcranFin::afficherJoueurGagnant(QString prenomJoueurGagnant)
{
    affichageJoueurGagnant->setText(prenomJoueurGagnant);
    affichageJoueurGagnant->setProperty("class", "configure");
    affichageJoueurGagnant->style()->polish(affichageJoueurGagnant);
}

void EcranFin::afficherStatistiques(QString prenomJoueurUn,
                                    QString prenomJoueurDeux,
                                    QString tirsJoueurUn,
                                    QString tirsJoueurDeux,
                                    QString billesBlanchesEmpocheesJoueurUn,
                                    QString billesBlanchesEmpocheesJoueurDeux,
                                    QString billesEmpocheesJoueurUn,
                                    QString billesEmpocheesJoueurDeux)
{
    affichageJoueurUn->setText(prenomJoueurUn);
    affichageJoueurDeux->setText(prenomJoueurDeux);
    affichageTirsJoueurUn->setText("Tirs : " + tirsJoueurUn);
    affichageTirsJoueurDeux->setText("Tirs : " + tirsJoueurDeux);
    affichageBillesBlanchesEmpocheesJoueurUn->setText(
      "Billes blanches Empochées : " + billesBlanchesEmpocheesJoueurUn);
    affichageBillesBlanchesEmpocheesJoueurDeux->setText(
      "Billes blanches Empochées : " + billesBlanchesEmpocheesJoueurDeux);
    affichageEmpochagesReussisJoueurUn->setText("Empochages Réussis : " +
                                                billesEmpocheesJoueurUn);
    affichageEmpochagesReussisJoueurDeux->setText("Empochages Réussis : " +
                                                  billesEmpocheesJoueurDeux);
}

void EcranFin::afficherDureePartie(QString duree)
{
    affichageDureePartie->setText("Durée de la partie : " + duree);
}
