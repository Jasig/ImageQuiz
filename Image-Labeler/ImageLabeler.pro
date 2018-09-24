QT += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Imagelabeler
TEMPLATE = app

win32: {
    DEFINES += _CRT_SECURE_NO_WARNINGS
    RC_FILE = imagelabeler.rc
}

macx: {
    QMAKE_LFLAGS += -Wl,-rpath,@loader_path/../,-rpath,@executable_path/../,-rpath,@executable_path/../Frameworks
    ICON = icon.icns
}

SOURCES += main.cpp\
    mainwindow.cpp \
    mytablewidget.cpp \
    myimageview.cpp \
    csvutil.cpp

HEADERS  += mainwindow.h \
    mytablewidget.h \
    myimageview.h \
    csvutil.hpp

FORMS += mainwindow.ui

RESOURCES += \
    res.qrc

win32:LIBS += gdi32.lib user32.lib
