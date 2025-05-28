QT       += core gui bluetooth

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

DEFINES += QT_DEPRECATED_WARNINGS
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    communicationbluetooth.cpp \
    ecranaccueil.cpp \
    ecranfin.cpp \
    ecranfinmatch.cpp \
    ecranmatch.cpp \
    ecranpartie.cpp \
    empoche.cpp \
    joueur.cpp \
    main.cpp \
    ecranpluginpool.cpp \
    manche.cpp \
    match.cpp \
    pluginpool.cpp

HEADERS += \
    communicationbluetooth.h \
    couleurbille.h \
    ecranaccueil.h \
    ecranfin.h \
    ecranfinmatch.h \
    ecranmatch.h \
    ecranpartie.h \
    ecranpluginpool.h \
    empoche.h \
    joueur.h \
    manche.h \
    match.h \
    pluginpool.h

# Les defines pour la version release (sans debug)
#CONFIG(release, debug|release):DEFINES+=QT_NO_DEBUG_OUTPUT RASPBERRY_PI
# Les defines pour la version debug
CONFIG(debug, debug|release):DEFINES+=DEBUG

RESOURCES += \
    ressource.qrc
