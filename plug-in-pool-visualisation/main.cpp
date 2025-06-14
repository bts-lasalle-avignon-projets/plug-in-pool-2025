#include "ecranpluginpool.h"
#include <QApplication>

/**
 * @file main.cpp
 * @brief Programme principal
 * @details Crée et affiche la fenêtre principale de l'application
 * EcranPlugInPool
 * @author NAVARRO Mattéo
 * @version 1.0
 *
 * @param argc
 * @param argv[]
 * @return int
 *
 */

int main(int argc, char* argv[])
{
    QApplication    a(argc, argv);
    EcranPlugInPool ecranPlugInPool;

    QFile file(":/pluginpool.css");
    if(file.open(QFile::ReadOnly | QFile::Text))
    {
        QTextStream in(&file);
        QString     style = in.readAll();
        a.setStyleSheet(style);
    }

    ecranPlugInPool.show();

    return a.exec();
}
