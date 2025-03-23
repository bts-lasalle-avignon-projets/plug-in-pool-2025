#ifndef EMPOCHE_H
#define EMPOCHE_H

#include "couleurbille.h"

class Empoche
{
  private:
    CouleurBille couleur;
    int          poche;

  public:
    Empoche(CouleurBille couleur, int poche);
};

#endif // EMPOCHE_H
