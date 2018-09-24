QT += core gui
greaterThan(QT_MAJOR_VERSION, 4) {
    QT += widgets
    QT += printsupport
}

TARGET = "IQ-Life Cycles"
TEMPLATE = app

INCLUDEPATH += $$PWD/../common

SOURCES  += main.cpp\
    quizapp.cpp \
    SelectImagesDialog.cpp \
    ../common/QuizOptionsDialog.cpp \
    ../common/QuizDialog.cpp \
    ../common/ProgressReport.cpp \
    ../common/AdvancedOptionsDialog.cpp \
    ../common/StudyOptionsDialog.cpp \
    ../common/StudyDialog.cpp \
    ../common/sysscale.cpp \
    ../common/sysfolders.cpp

HEADERS  += quizapp.h \
    SelectImagesDialog.h \
    ../common/QuizOptionsDialog.h \
    ../common/QuizDialog.h \
    ../common/ProgressReport.h \
    ../common/AdvancedOptionsDialog.h \
    ../common/StudyOptionsDialog.h \
    ../common/StudyDialog.h \
    ../common/sysscale.h \
	../common/sysfolders.h

macx {
    LIBS += -framework Foundation
    SOURCES += ../common/macfolders.m

    QMAKE_LFLAGS += -Wl,-rpath,@loader_path/../,-rpath,@executable_path/../,-rpath,@executable_path/../Frameworks

    QMAKE_INFO_PLIST = Info.plist
}

win32:LIBS += gdi32.lib user32.lib
win32:RC_FILE += IQ.rc


