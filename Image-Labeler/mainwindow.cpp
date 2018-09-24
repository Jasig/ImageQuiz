#include "mainwindow.h"
#include "ui_mainwindow.h"

#include "csvutil.hpp"

#include <QGridLayout>
#include <QLayout>

#include <QString>
#include <QDir>
#include <QWidget>
#include <QTabWidget>


#include <QFileDialog>
#include <QMessageBox>
#include <QInputDialog>
#include <QLineEdit>

#include <QList>
#include <QDebug>

QString dbDir  = "Images"; // default directory
QString dbName = "database.csv"; // default db name
QString imgExt = ".jpg"; // image extension

static
void
string_filter_digits(const QString & string, char * buffer, size_t size)
{
    char stringSource[1024];
    memset(stringSource, 0, sizeof stringSource);

    strncpy(stringSource, string.toStdString().c_str(), sizeof stringSource);

    const char * s = stringSource;
    char * d = buffer;
    while (*s != '\0' && (d - buffer < size))
    {
        if (*s >= '0' && *s <= '9')
            *(d++) = *s;
        else
            *(d++) = '0';
        ++s;
    }
}

static
void
string_filter_nondigits(const QString & string, char * buffer, size_t size)
{
    char stringSource[1024];
    memset(stringSource, 0, sizeof stringSource);

    strncpy(stringSource, string.toStdString().c_str(), sizeof stringSource);

    const char * s = stringSource;
    char * d = buffer;
    while (*s != '\0' && (d - buffer < size))
    {
        if (!(*s >= '0' && *s <= '9'))
            *(d++) = *s;
        else
            *(d++) = ' ';
        ++s;
    }
}

static
bool
string_custom_less_than(const QString & lhs, const QString & rhs)
{
    char lhsStringPart[1024];
    memset(lhsStringPart, 0, sizeof lhsStringPart);

    string_filter_nondigits(lhs.toLower(), lhsStringPart, sizeof lhsStringPart);

    char lhsNumberPart[1024];
    memset(lhsNumberPart, 0, sizeof lhsNumberPart);

    string_filter_digits(lhs.toLower(), lhsNumberPart, sizeof lhsNumberPart);

    char rhsStringPart[1024];
    memset(rhsStringPart, 0, sizeof rhsStringPart);

    string_filter_nondigits(rhs.toLower(), rhsStringPart, sizeof rhsStringPart);

    char rhsNumberPart[1024];
    memset(rhsNumberPart, 0, sizeof rhsNumberPart);

    string_filter_digits(rhs.toLower(), rhsNumberPart, sizeof rhsNumberPart);

    int lhsInt = atoi(lhsNumberPart);
    int rhsInt = atoi(rhsNumberPart);

    QString lhsString = QString::fromUtf8(lhsStringPart).trimmed();
    QString rhsString = QString::fromUtf8(rhsStringPart).trimmed();

    if (lhsString == rhsString)
        return lhsInt < rhsInt;

    return lhs.toLower() < rhs.toLower();
}


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    dbDirtyFlag     = false;


    openDirectory(); // process db directory


    tab = new QTabWidget (ui->centralWidget); // tabs for all tables

    QString tabCaptions[3] =
    {
        "New images (%1)",
        "Incomplete entries (%1)",
        "Complete entries (%1)"
    };

    for (int i =0; i < 3; ++i)
    {
        tableSet[i] = new myTableWidget(ui->centralWidget);

        tab->addTab(tableSet[i], tabCaptions[i].arg(dataSet[i].count()));

        tableSet[i]->setData(&fieldNames,&dataSet[i]); // link data <-> tables

        connect(tableSet[i], SIGNAL(imageChanged(QString)), this, SLOT(imageChanged(QString)));
        connect(tableSet[i], SIGNAL(cellChanged(int,int)), this, SLOT(dbDataChanged()));
        connect(tableSet[i], SIGNAL(renameFieldRequested(int)), this, SLOT(renameField(int)));
        connect(tableSet[i], SIGNAL(renameImageRequested(QString,QString)), this, SLOT(renameFile(QString, QString)));
    }

    connect(tab, SIGNAL(currentChanged (int)), this, SLOT(tabIndexChanged(int)));

    QGridLayout *layout = new QGridLayout(ui->centralWidget); // set widgets positioning

    layout->addWidget (tab, 0, 0);

    image = new myImageView(ui->dockWidget); // use label to display images
    ui->dockWidget->setWidget(image);

    QImage noimg(":/images/logo");
    image->setPixmap(QPixmap::fromImage(noimg));

    if (dataSet[NEW].count() > 0) // we have new images to add
        dbDirtyFlag = true;

    if (dataSet[NEW].count() == 0)
    {
        if (dataSet[PARTIAL].count()== 0)
            tab->setCurrentIndex(COMPLETE); // no new & part images - goto complete tab
        else
            tab->setCurrentIndex(PARTIAL); // no new images - go to incomplete tab
    }

    this->show();
}

MainWindow::~MainWindow()
{
    delete ui;
}


void MainWindow::imageChanged(QString imageName)
{
    ui->dockWidget->setWindowTitle(imageName);

    if (imageName.isEmpty())
    {
        image->clear();
        return;
    }

    QString s = QDir(dbDir).filePath(imageName + imgExt);
    s = QDir::toNativeSeparators(s);

    QImage img(s);

    if (img.isNull())
    {
        QImage noimg(":/images/noimg");
        image->setPixmap(QPixmap::fromImage(noimg));

        ui->statusBar->showMessage("Can't load image: " + s, 10000);

        return;
    }

    image->setPixmap(QPixmap::fromImage(img));
}

void MainWindow::tabIndexChanged(int index)
{
    tableSet[index]->refreshImage();
}

void MainWindow::on_actionSave_triggered()
{
    for (int i = 0; i < 3; ++i)
    {
        tableSet[i]->setDisabled(true);
        tableSet[i]->setDisabled(false);
    }

    writeDatabase();
    dbDirtyFlag = false;
}

void MainWindow::on_actionRename_field_triggered()
{
    myTableWidget *table = tableSet[tab->currentIndex()];

    renameField(table->currentColumn());
}


void MainWindow::on_actionAdd_field_triggered()
{
    QString s = QInputDialog::getText(this, "Add new field", "Field name", QLineEdit::Normal, "New field");

    s = csv_parse_field(s);

    if (s.isEmpty())
        return;

    myTableWidget * table = tableSet[tab->currentIndex()];

    int index = table->currentColumn();

    if(index == -1)
        index = fieldNames.count();
    else
        index += 1; // add field to the right from the current column

    fieldNames.insert(index,s);


    for (int i = 0; i < 3; ++i)
    {
        tableSet[i]->insertColumn(index);
        tableSet[i]->setHorizontalHeaderLabels(fieldNames);

        for (int j = 0; j < dataSet[i].count(); ++j)
            dataSet[i][j].insert(index,"");
    }

    dbDirtyFlag = true;
}


void MainWindow::dbDataChanged()
{
    dbDirtyFlag = true;
}

void MainWindow::renameField(int index)
{
    if (index != -1)
    {
        QString s = QInputDialog::getText(this, "Rename field", "Field name", QLineEdit::Normal, fieldNames.at(index));
        s = csv_parse_field(s);

        if (!s.isEmpty())
        {
            fieldNames.replace(index,s);
            for (int i=0; i < 3; ++i)
                tableSet[i]->setHorizontalHeaderLabels(fieldNames);

            dbDirtyFlag = true;
        }
    }
}



void MainWindow::on_actionRemove_field_triggered()
{
    myTableWidget *table = tableSet[tab->currentIndex()];

    int index = table->currentColumn();

    if(index == -1 || index == 0) // no column selected, col 0 can't be removed
        return;

    QString msg = "Are you sure that you want to permanently delete the '%1' field and all data in that column?\n";

    QMessageBox::StandardButton resBtn = QMessageBox::question( this, "Confirmation", msg.arg(fieldNames.at(index)),
                                                            QMessageBox::No | QMessageBox::Yes,
                                                            QMessageBox::No);

    if (resBtn == QMessageBox::Yes)
    {
        fieldNames.removeAt(index);

        for(int i=0; i<3; ++i)
        {
            tableSet[i]->removeColumn(index);
            tableSet[i]->setHorizontalHeaderLabels(fieldNames);

            for (int j = 0; j < dataSet[i].count(); ++j)
                dataSet[i][j].removeAt(index);
        }

        dbDirtyFlag = true;
    }
}




void MainWindow::closeEvent (QCloseEvent *event)
{
    if (dbDirtyFlag) // db has been changed
    {
        QMessageBox::StandardButton resBtn = QMessageBox::question( this, "Confirmation",
                                                                tr("Do you want to save the database before exit?\n"),
                                                                QMessageBox::No | QMessageBox::Yes,
                                                                QMessageBox::Yes);

        if (resBtn == QMessageBox::Yes)
            on_actionSave_triggered();
    }

    event->accept();
}




int MainWindow::openDirectory()
{
    if (!QDir(dbDir).exists())
    {
        dbDir = QFileDialog::getExistingDirectory(this, tr("Open Directory"), "", QFileDialog::ShowDirsOnly);
    }

    QStringList nameFilter("*" + imgExt); // create file mask ("*.jpg")
    QDir directory(dbDir);
    imageFileList = directory.entryList(nameFilter);

    for (int i = 0; i < imageFileList.count(); ++i) // remove extesions
        imageFileList[i] = imageFileList[i].section(".", 0, 0);

    // qSort(imageFileList.begin(), imageFileList.end(), string_custom_less_than);
    imageFileList.sort();

    QFile dbFile(QDir(dbDir).filePath(dbName));

    if (dbFile.exists())
        readDatabase(); // read database.csv
    else
        fieldNames.append("File name"); // Create 1st field name in new db

    // adding new images to newEntriesList
    for (int i = 0; i < imageFileList.count(); ++i)
    {
        if (!dbimageFileList.contains(imageFileList.at(i))) // image file not found in DB
        {
            QStringList a(imageFileList.at(i));
            for (int j = 0; j < fieldNames.count() - 1; ++j)
                a.append("");

            dataSet[NEW].append(a);
        }
    }

    return 0;
}

int MainWindow::readDatabase()
{
    QString s;
    QStringList sl;

    QFile dbFile(QDir(dbDir).filePath(dbName));

    dbFile.open(QIODevice::ReadOnly);
    QTextStream inputstream(&dbFile);
    s = inputstream.readAll();
    dbFile.close();

    QStringList lines (s.split(QRegExp("\r\n|\n|\r"), QString::SkipEmptyParts)); // Win, Unix and Mac newline

    QChar sep = ','; // default field separator

    fieldNames = lines[0].split(sep);

    // building list of filenames in db
    for (int i = 1; i < lines.count(); ++i)
    {
        sl = lines.at(i).split(sep);
        if (sl.at(0) != "")
        {
            dbimageFileList.append(sl.at(0));

            while (sl.count() < fieldNames.count())
                sl.append("");

            bool flag = false;

            for (int j = 0; j < fieldNames.count(); ++j)
            {
                if (sl.at(j) == "")
                    flag = true;
            }

            if (!flag)
                dataSet[COMPLETE].append(sl); // completeEntriesList.append(sl);
            else
                dataSet[PARTIAL].append(sl); // partEntriesList.append(sl);

        }
    }

    ui->statusBar->showMessage("Database loaded", 10000);

    return 0;
}


int MainWindow::writeDatabase()
{
    QList<QStringList> saveEntriesList;

    for (int i = 0; i < 3; ++i)
        saveEntriesList.append(dataSet[i]);

    QStringList lines;
    for (int i = 0; i < saveEntriesList.count(); ++i)
        lines.append(saveEntriesList.at(i).join(","));

    QString result(fieldNames.join(",") + "\n");

    result.append(lines.join("\n"));

    QFile dbFile(QDir(dbDir).filePath(dbName));

    dbFile.open(QIODevice::WriteOnly | QIODevice::Text);
    QTextStream outstream(&dbFile);

    outstream << result;

    dbFile.close();

    ui->statusBar->showMessage("Database saved successfuly", 10000);
    return 0;
}


void MainWindow::renameFile(QString oldname, QString newname)
{
    oldname = QDir(dbDir).filePath(oldname+imgExt);
    newname = QDir(dbDir).filePath(newname+imgExt);

    bool result = QFile::rename(oldname, newname);

    if (result)
    {
        writeDatabase(); // flush DB to disk to make sure that everything will be in sync
    }
    else
    {
        QString msg = "Error renaming \n'%1'\n->\n'%2'\n";

        ui->statusBar->showMessage(msg.arg(oldname,newname),10000);
    }
}
