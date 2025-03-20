#include "ecranpluginpool.h"

#include <QApplication>

int main(int argc, char* argv[])
{
    QApplication    a(argc, argv);
    EcranPlugInPool w;
    w.show();
    return a.exec();
}
