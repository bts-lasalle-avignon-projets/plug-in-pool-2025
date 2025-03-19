#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>

class CommunicationBluetooth : public QObject
{
    Q_OBJECT
public:
    explicit CommunicationBluetooth(QObject *parent = nullptr);

signals:

};

#endif // COMMUNICATIONBLUETOOTH_H
