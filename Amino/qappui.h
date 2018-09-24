#pragma once

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QHeaderView>
#include <QtGui/QMainWindow>
#include <QtGui/QMenuBar>
#include <QtGui/QStatusBar>
#include <QtGui/QToolBar>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_QAppClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *QAppClass)
    {
        if (QAppClass->objectName().isEmpty())
            QAppClass->setObjectName(QString::fromUtf8("QAppClass"));
        QAppClass->resize(600, 400);
        menuBar = new QMenuBar(QAppClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        QAppClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(QAppClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        QAppClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(QAppClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        QAppClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(QAppClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        QAppClass->setStatusBar(statusBar);

        retranslateUi(QAppClass);

        QMetaObject::connectSlotsByName(QAppClass);
    } // setupUi

    void retranslateUi(QMainWindow *QAppClass)
    {
        QAppClass->setWindowTitle(QApplication::translate("QAppClass", "QApp", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class QAppClass: public Ui_QAppClass {};
} // namespace Ui

QT_END_NAMESPACE
