/**
 * @file ecranpluginpool.h
 *
 * @brief Déclaration de la classe EcranPlugInPool
 * @author NAVARRO Mattéo
 * @version 1.0
 */

#ifndef ECRANPLUGINPOOL_H
#define ECRANPLUGINPOOL_H

#include <QStackedWidget>
#include <QVBoxLayout>
#include <QtWidgets>
#include <ecranaccueil.h>
#include <ecranmatch.h>
#include <ecranfin.h>

/**
 * @def NOM_APPLICATION
 * @brief Le nom de l'application
 */
#define NOM_APPLICATION "PlugInPool"

/**
 * @def VERSION_APPLICATION
 * @brief La version de l'application
 */
#define VERSION_APPLICATION "1.0"

#ifndef RASPBERRY_PI
/**
 * @def LARGEUR_ECRAN
 * @brief La largeur de l'écran par défaut
 */
#define LARGEUR_ECRAN 1920

/**
 * @def HAUTEUR_ECRAN
 * @brief La hauteur de l'écran par défaut
 */
#define HAUTEUR_ECRAN 1080
#endif

class PlugInPool;

/**
 * @class EcranPlugInPool
 * @brief Déclaration de la classe EcranPlugInPool
 * @details Cette classe gère l'interface graphique de l'application PlugInPool
 */
class EcranPlugInPool : public QWidget
{
    Q_OBJECT
  private:
    PlugInPool*     plugInPool; //!< association vers PlugInPool
    QStackedWidget* ecransInterface;
    EcranAccueil*   ecranAccueil;
    EcranMatch*     ecranMatch;
    EcranFin*       ecranFin;

    QVBoxLayout* interfacePlugInPool;

  public:
    EcranPlugInPool(QWidget* parent = nullptr);
    ~EcranPlugInPool();

    void afficherEcranAccueil();
    void afficherEcranMatch();
    void afficherEcranFin();
};

#endif // ECRANPLUGINPOOL_H
