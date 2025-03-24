#include "match.h"

Match::Match(const QString& nom, int nbManchesGagnantes) :
    nom(nom), nbManchesGagnantes(nbManchesGagnantes),
    horodatage(QDateTime::currentDateTime()), etat(EtatMatch::Cree),
    gagnant(AUCUN_GAGNANT)
{
}
