#ifndef MATCH_H
#define MATCH_H

#include <QString>
#include <QDateTime>

class Match
{
  private:
    enum EtatMatch
    {
        Cree    = 0,
        EnCours = 1,
        Fini    = 2
    };
    QString*   nom;
    int        nbManchesGagnantes;
    QDateTime* horodatage;
    EtatMatch  etat;
    int        gagnant;

  public:
    Match();
};

#endif // MATCH_H
