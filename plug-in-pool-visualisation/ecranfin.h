#ifndef ECRANFIN_H
#define ECRANFIN_H

#include <QWidget>

class EcranFin : public QObject
{
    Q_OBJECT
  private:
    QWidget* ecran;

  public:
    explicit EcranFin(QWidget* parent = nullptr);
    QWidget* getEcran() const;

  signals:
};

#endif // ECRANFIN_H
