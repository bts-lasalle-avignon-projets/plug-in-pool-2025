#ifndef ECRANMATCH_H
#define ECRANMATCH_H

#include <QWidget>

class EcranMatch : public QObject
{
    Q_OBJECT
  private:
    QWidget* ecran;

  public:
    explicit EcranMatch(QWidget* parent = nullptr);
    QWidget* getEcran() const;

  signals:
};

#endif // ECRANMATCH_H
