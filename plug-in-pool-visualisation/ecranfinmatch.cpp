#include "ecranfinmatch.h"

EcranFinMatch::EcranFinMatch(QWidget* parent) : QWidget(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageVersion       = new QLabel("v0.1", ecran);
    affichageJoueurGagnant = new QLabel("Aucun joueur gagnant", ecran);
    affichageScores        = new QLabel("Aucun Score", ecran);

    affichageVersion->setObjectName("affichageVersion");
    affichageJoueurGagnant->setObjectName("affichageJoueurGagnant");
    affichageScores->setObjectName("affichageScores");

    QVBoxLayout* ecranFinMatch       = new QVBoxLayout(ecran);
    QHBoxLayout* espaceVersion       = new QHBoxLayout();
    QHBoxLayout* espaceJoueurGagnant = new QHBoxLayout();
    QHBoxLayout* espaceScores        = new QHBoxLayout();

    espaceVersion->addStretch();
    espaceVersion->addWidget(affichageVersion);
    espaceVersion->addStretch();

    espaceJoueurGagnant->addStretch();
    espaceJoueurGagnant->addWidget(affichageJoueurGagnant);
    espaceJoueurGagnant->addStretch();

    espaceScores->addStretch();
    espaceScores->addWidget(affichageScores);
    espaceScores->addStretch();

    ecranFinMatch->addSpacing(150);
    ecranFinMatch->addLayout(espaceVersion);
    ecranFinMatch->addSpacing(460);
    ecranFinMatch->addLayout(espaceJoueurGagnant);
    ecranFinMatch->addSpacing(170);
    ecranFinMatch->addLayout(espaceScores);
    ecranFinMatch->addStretch();
}

EcranFinMatch::~EcranFinMatch()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranFinMatch::getEcran() const
{
    return ecran;
}

void EcranFinMatch::afficherJoueurGagnant(QString prenomJoueurGagnant)
{
    affichageJoueurGagnant->setText(prenomJoueurGagnant);
    affichageJoueurGagnant->setProperty("class", "configure");
    affichageJoueurGagnant->style()->polish(affichageJoueurGagnant);
}

void EcranFinMatch::afficherScores(QString scores)
{
    affichageScores->setText(scores);
}
