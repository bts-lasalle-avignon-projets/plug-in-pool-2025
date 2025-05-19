#include "ecranfin.h"
#include <QDebug>

EcranFin::EcranFin(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    qDebug() << Q_FUNC_INFO << this;

    affichageVersion       = new QLabel("v1.0", ecran);
    affichageJoueurGagnant = new QLabel("Aucun joueur gagnant", ecran);

    affichageVersion->setObjectName("affichageVersion");
    affichageJoueurGagnant->setObjectName("affichageJoueurGagnant");

    QVBoxLayout* ecranFin            = new QVBoxLayout(ecran);
    QHBoxLayout* espaceVersion       = new QHBoxLayout();
    QHBoxLayout* espaceJoueurGagnant = new QHBoxLayout();

    espaceVersion->addStretch();
    espaceVersion->addWidget(affichageVersion);

    espaceJoueurGagnant->addStretch();
    espaceJoueurGagnant->addWidget(affichageJoueurGagnant);
    espaceJoueurGagnant->addStretch();

    ecranFin->addLayout(espaceVersion);
    ecranFin->addSpacing(600);
    ecranFin->addLayout(espaceJoueurGagnant);
    ecranFin->addSpacing(80);
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
