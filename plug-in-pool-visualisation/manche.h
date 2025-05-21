#ifndef MANCHE_H
#define MANCHE_H

#include <QDateTime>
#include <QList>
#include "joueur.h"

class Manche
{
  private:
    int           numeroTable;
    QDateTime     horodatage;
    int           gagnant;
    int           joueurTable;
    bool          couleursDefinies;
    int           duree;
    QList<Joueur> joueurs;

  public:
    Manche(int numeroTable);
};

#endif // MANCHE_H
