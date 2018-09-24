#include "sysfolders.h"

#include <QCoreApplication>
#include <QDir>
#include <QDesktopServices>
#include <QDebug>

#if QT_VERSION >= QT_VERSION_CHECK(5, 0, 0)
    #include <QStandardPaths>
#endif

#ifdef __APPLE__
extern "C"
void
mac_resource_folder(char * buffer, size_t size);
#endif

std::string
UserDataFolder()
{
    QDir base = QDir(QCoreApplication::applicationDirPath());

#if defined(__APPLE__)
    base.cdUp();
    base.cdUp();
    base.cdUp();
#endif

    if (!base.exists("User Files"))
    {
        if (!base.mkdir("User Files"))
            qDebug() << "couldn't create user data folder in " << base.path();
    }

    base.cd("User Files");

    return base.path().toStdString() + "/";
}

std::string
ResourceFolder()
{
    QDir base = QDir(QCoreApplication::applicationDirPath());

#if defined(__APPLE__)
    base.cdUp();
    base.cdUp();
    base.cdUp();
#endif

    return (base.absolutePath() + "/").toStdString();
}
