#include "ecranfin.h"
#include <QDebug>

EcranFin::EcranFin(QWidget* parent) : QObject(parent), ecran(parent)
{
    qDebug() << Q_FUNC_INFO << this;
}

EcranFin::~EcranFin()
{
    qDebug() << Q_FUNC_INFO << this;
}

QWidget* EcranFin::getEcran() const
{
    return ecran;
}
