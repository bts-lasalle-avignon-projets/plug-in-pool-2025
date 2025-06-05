#include "ecranfin.h"
#include <QDebug>

EcranFin::EcranFin(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageVersion       = new QLabel("v1.0", ecran);
    affichageJoueurGagnant = new QLabel("Aucun joueur gagnant", ecran);

    affichageJoueurUn                        = new QLabel(ecran);
    affichageTirsJoueurUn                    = new QLabel(ecran);
    affichageBillesBlanchesEmpocheesJoueurUn = new QLabel(ecran);
    affichageEmpochagesReussisJoueurUn       = new QLabel(ecran);

    affichageJoueurDeux                        = new QLabel(ecran);
    affichageTirsJoueurDeux                    = new QLabel(ecran);
    affichageBillesBlanchesEmpocheesJoueurDeux = new QLabel(ecran);
    affichageEmpochagesReussisJoueurDeux       = new QLabel(ecran);

    affichageVersion->setObjectName("affichageVersion");
    affichageJoueurGagnant->setObjectName("affichageJoueurGagnant");

    affichageJoueurUn->setObjectName("affichageJoueurUn");
    affichageTirsJoueurUn->setObjectName("affichageTirsJoueurUn");
    affichageBillesBlanchesEmpocheesJoueurUn->setObjectName(
      "affichageBillesBlanchesEmpocheesJoueurUn");
    affichageEmpochagesReussisJoueurUn->setObjectName(
      "affichageEmpochagesReussisJoueurUn ");

    affichageJoueurDeux->setObjectName("affichageJoueurDeux");
    affichageTirsJoueurDeux->setObjectName("affichageTirsJoueurUn");
    affichageBillesBlanchesEmpocheesJoueurDeux->setObjectName(
      "affichageBillesBlanchesEmpocheesJoueurUn");
    affichageEmpochagesReussisJoueurDeux->setObjectName(
      "affichageEmpochagesReussisJoueurUn ");

    QVBoxLayout* ecranFin                     = new QVBoxLayout(ecran);
    QHBoxLayout* espaceVersion                = new QHBoxLayout();
    QHBoxLayout* espaceJoueurGagnant          = new QHBoxLayout();
    QHBoxLayout* espaceStatistiques           = new QHBoxLayout();
    QVBoxLayout* espaceStatistiquesJoueurUn   = new QVBoxLayout();
    QVBoxLayout* espaceStatistiquesJoueurDeux = new QVBoxLayout();

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

    espaceStatistiques->addLayout(espaceStatistiquesJoueurUn);
    espaceStatistiques->addLayout(espaceStatistiquesJoueurDeux);

    ecranFin->addStretch();
    ecranFin->addLayout(espaceVersion);
    ecranFin->addSpacing(480);
    ecranFin->addLayout(espaceJoueurGagnant);
    ecranFin->addSpacing(160);
    ecranFin->addLayout(espaceStatistiques);
    ecranFin->addStretch();
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
