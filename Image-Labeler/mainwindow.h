#ifndef MAINWINDOW_H
#define MAINWINDOW_H



#include <QMainWindow>
#include <QLabel>
#include <QTableWidget>
#include <QList>
#include <QCloseEvent>

#include "mytablewidget.h"
#include "myimageview.h"

#define NEW 0
#define PARTIAL 1
#define COMPLETE 2


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    void closeEvent (QCloseEvent *event);



private slots:
    void imageChanged(QString imagename);
    void dbDataChanged();
    void tabIndexChanged(int index);
    void renameField(int index);
    void renameFile(QString oldname, QString newname);


    void on_actionSave_triggered();
    void on_actionRename_field_triggered();
    void on_actionAdd_field_triggered();
    void on_actionRemove_field_triggered();

private:
    bool            dbDirtyFlag;

    int             openDirectory();
    int             createDatabase();
    int             readDatabase();
    int             writeDatabase();


    myImageView     *image;

    myTableWidget   *tableSet[3];

    QTabWidget      *tab;

    QStringList     fieldNames;


    QStringList     imageFileList;
    QStringList     dbimageFileList;


    QList<QStringList>          dbEntriesList;

    QStringList                 dataName;
    QList<QStringList>          dataSet[3];




    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_H
