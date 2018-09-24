QT += core gui
greaterThan(QT_MAJOR_VERSION, 4) {
    QT += widgets
    QT += printsupport
}

TARGET = Amino
TEMPLATE = app

INCLUDEPATH += $$PWD/../common

DEFINES += "APP_AMINO"

SOURCES += main.cpp\
    quizapp.cpp \
    SelectAminoAcidDialog.cpp \
	AminoAcidList.cpp \
    AminoAcid.cpp \
	../common/StudyOptionsDialog.cpp \
    ../common/StudyDialog.cpp \
    ../common/QuizOptionsDialog.cpp \
    ../common/QuizDialog.cpp \
    ../common/ProgressReport.cpp \
    ../common/AdvancedOptionsDialog.cpp \
	../common/sysfolders.cpp \
    ../common/sysscale.cpp

macx {
    LIBS += -framework Foundation
    SOURCES += ../common/macfolders.m

    QMAKE_LFLAGS += -Wl,-rpath,@loader_path/../,-rpath,@executable_path/../,-rpath,@executable_path/../Frameworks

    QMAKE_INFO_PLIST = Info.plist
}

HEADERS += quizapp.h \
    SelectAminoAcidDialog.h \
    AminoAcidList.h \
    AminoAcid.h \
    ../common/StudyOptionsDialog.h \
    ../common/StudyDialog.h \
    ../common/QuizOptionsDialog.h \
    ../common/QuizDialog.h \
    ../common/ProgressReport.h \
    ../common/AdvancedOptionsDialog.h \
    ../common/sysfolders.h \
	../common/sysscale.h

win32:LIBS += gdi32.lib user32.lib
win32:RC_FILE += Amino.rc


