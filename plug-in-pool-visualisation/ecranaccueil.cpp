#include "ecranaccueil.h"
#include <QFile>
#include <QTextStream>
#include <QDebug>

EcranAccueil::EcranAccueil(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;

    affichageVersion   = new QLabel("v0.1", ecran);
    connexionBluetooth = new QLabel("Connexion en cours ...", ecran);

    affichageVersion->setObjectName("affichageVersion");
    connexionBluetooth->setObjectName("connexionBluetooth");

    QVBoxLayout* ecranAccueil             = new QVBoxLayout(ecran);
    QHBoxLayout* espaceVersion            = new QHBoxLayout();
    QHBoxLayout* espaceConnexionBluetooth = new QHBoxLayout();

    espaceVersion->addStretch();
    espaceVersion->addWidget(affichageVersion);
    espaceVersion->addStretch();

    espaceConnexionBluetooth->addStretch();
    espaceConnexionBluetooth->addWidget(connexionBluetooth);
    espaceConnexionBluetooth->addStretch();

    /**
     * @todo Est-ce vraiment la bonne technique ?
     */
    ecranAccueil->addStretch();
    ecranAccueil->addLayout(espaceVersion);
    ecranAccueil->addSpacing(640);
    ecranAccueil->addLayout(espaceConnexionBluetooth);
    ecranAccueil->addStretch();
}

EcranAccueil::~EcranAccueil()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranAccueil::getEcran() const
{
    return ecran;
}

void EcranAccueil::afficherEtatConnexion(QString message, bool etat)
{
    connexionBluetooth->setText(message);
    if(etat)
        connexionBluetooth->setProperty("class", "connecte");
    else
        connexionBluetooth->setProperty("class", "deconnecte");
    connexionBluetooth->style()->polish(connexionBluetooth);
}
