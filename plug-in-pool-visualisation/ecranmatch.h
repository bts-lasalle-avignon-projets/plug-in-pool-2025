#ifndef ECRANMATCH_H
#define ECRANMATCH_H

#include <QtWidgets>

class EcranMatch : public QWidget
{
    Q_OBJECT
  private:
    QWidget* ecran;
    QLabel*  affichageJoueurUn;
    QLabel*  affichageJoueurDeux;
    QLabel*  affichageNombreParties;

  public:
    explicit EcranMatch(QWidget* parent = nullptr);
    virtual ~EcranMatch();
    QWidget* getEcran() const;

    void afficherInformationsMatch(int     nbManches,
                                   QString joueur1,
                                   QString joueur2);

  signals:
};

#endif // ECRANMATCH_H
