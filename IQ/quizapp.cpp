#ifdef _WIN32
#include <Windows.h>
#endif

#include <QtGui>

#include <QAction>
#include <QFile>
#include <QKeyEvent>
#include <QMainWindow>
#include <QMenu>
#include <QMenuBar>
#include <QPixmap>
#include <QString>
#include <QTextStream>
#include <QStringList>
#include <QPushButton>
#include <QCheckBox>
#include <QStringList>
#include <QDesktopServices>
#include <QUrl>

#include "quizapp.h"
#include "SelectImagesDialog.h"
#include <QuizOptionsDialog.h>
#include <StudyOptionsDialog.h>
#include <QuizDialog.h>
#include <StudyDialog.h>
#include <AdvancedOptionsDialog.h>
#include <ProgressReport.h>
#include <sysfolders.h>
#include <sysscale.h>

#define MIN_WIDTH ppp(630)
#define MIN_HEIGHT ppp(200)
#define MAX_MODES 4

const char *mode[ MAX_MODES ] = {
   "Image Naming with Prompt",
   "Image Naming without Prompt",
   "Image Comparison",
   "Image Verification"
};

// Write user data to file
void QuizApp::writeUserData(QStringList records)
{
    QFile outFile;
    outFile.setFileName(fname + ".csv");

    // If for whatever reason the current user file could
    // not be opened the current user session info will not be saved.
    if (!outFile.open(QIODevice::WriteOnly | QIODevice::Text))
    {
        QMessageBox::warning(this, "Error", tr("<b>Could NOT save this session</b>"));
            return;
    }
    
    QTextStream out;
    out.setDevice(&outFile);

    // First record is Advanced settings.
    // Every other line after that is a progress report
    for (int i = 0; i < records.size(); ++i)
    {
        out << records[ i ].trimmed();
        out << "\n";
    }
    outFile.close();
}

// Create a String List to be written to file
// It ensures only upto MAX_REPORTS number of reports are saved to file.
void QuizApp::updateUserData()
{
    // Creating string list 'records' that will store the advanced options
    // of the current session followed by the current progress reports and
    // then the rest of the progress reports read from the existing user file.

    QStringList records;

    // Save advanced options
    records << AdvancedOptionsDialog::instance()->getSettings();

    // Save last modified date of the database
    QFileInfo fi((ResourceFolder() + "Database.csv").c_str());
    records << fi.lastModified().toString() + "\n";
    QStringList currentRecords = QuizDialog::instance()->getCurrentRecords();
    int reportCounter = 0;    // Counter to ensure no more than
                              // MAX_REPORTS # of reports are saved
    int n = currentRecords.size();
    
    // If there are progress reports of
    // the currect session save those
    if (n != 0)
    {
        // in the for loop we also check to make sure
        // that we do not exceed MAX_REPORT number of progress reports
        for (int i = n - 1; i >= 0 && reportCounter < MAX_REPORTS; --i)
        {
            records << currentRecords[i];
            reportCounter++;
        }
    }
    
    // After reading saving all the data for the current session
    // we append to the end of the list all the previous user data
    QFile inFile;
    inFile.setFileName(fname + ".csv");
    if (!inFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning(this, "Error", tr("<b>Cannot open %1 for reading.</b>" ).arg(fname + ".csv"));
        inFile.close();
      
        // If the saved user file could not be read
        // try to save the current session.
        writeUserData(records);
        return;
    }

    QTextStream in;
    in.setDevice(&inFile);
    QString line;
    in.readLine();    // Ignore the advanced options
    in.readLine();    // Ignore the database modification time.
    
    // Keep reading until we reach the end of the
    // file or we have saved MAX_REPORTS reports.
    while (!in.atEnd() && reportCounter < MAX_REPORTS)
    {
        line = in.readLine();
        if (line != "")
        {
            records << line;
            reportCounter++;
        }
        else
            break;
    }
    
    inFile.close();
    
    // Write all the data back to the user file
    writeUserData(records);
}

// View the Progress Report
void QuizApp::viewProgress()
{
    qDebug() << "generating report '" << fname << "'";

    // move the dialog to the center of the screen
    moveDialog(ProgressReport::instance());
    ProgressReport::instance()->generateReport(QuizDialog::instance()->getCurrentRecords(), fname);
    ProgressReport::instance()->exec();
}

// Show help page
void QuizApp::showHelp()
{
    QString urlString = ResourceFolder().c_str();
    urlString = "file:///" + urlString + "Help/";
    urlString += "Help.html";
    QDesktopServices::openUrl(QUrl(urlString));;
}

// Show tutorial page
void QuizApp::showTutorial()
{
    QString urlString = ResourceFolder().c_str();
    urlString = "file:///" + urlString + "Help/";
    urlString += "Tutorial.html";
    QDesktopServices::openUrl(QUrl(urlString));
}

// Reset the centers of the dialogs
// when the main dialog is repositioned
void QuizApp::updateCenter()
{
    QRect rect = geometry();
    center.setX(rect.center().x());
    center.setY(rect.center().y());

    ProgressReport::instance(this)->setCoordinates(center.x(), center.y());
    StudyOptionsDialog::instance(this)->setCoordinates(center.x(), center.y());
    QuizOptionsDialog::instance(this)->setCoordinates(center.x(), center.y());
}

// Called when the main dialog is resized
void QuizApp::resizeEvent(QResizeEvent *)
{
    QSize windowSize = size();
    int x = windowSize.width() / 2;
    int y = windowSize.height() / 2;

    // If the user is not logged in then
    // setup login widgets otherwise quiz
    // and study widgets.
    if (!loggedIn)
    {
        int width = ppp(300);
        int height = ppp(100);
        int separation = ppp(5);

        loginGroupBox->setGeometry(QRect(x - width - separation, y - height / 2, width, height));
        loginGroupBox->show();

        newUserGroupBox->setGeometry(QRect(x + separation, y - height / 2, width, height));
        newUserGroupBox->show();
    }
    else
    {
        int buttonWidth = ppp(300);
        int buttonHeight = ppp(75);

        selectionButton->setGeometry(QRect(x - buttonWidth - ppp(5), y - buttonHeight - ppp(5 + 10), buttonWidth, buttonHeight));

        studyButton->setGeometry(QRect(x + ppp(5), y - buttonHeight - ppp(5 + 10), buttonWidth, buttonHeight));

        quizButton->setGeometry(QRect(x - buttonWidth - ppp(5), y + ppp(5 + 10), buttonWidth, buttonHeight));

        testButton->setGeometry(QRect(x + ppp(5), y + ppp(5 + 10), buttonWidth, buttonHeight));
    }

    updateCenter();

    repaint();
}

// Called when the main dialog is moved
void QuizApp::moveEvent(QMoveEvent *)
{
    updateCenter();
}

// Called to reposition the dialog
// so that it is in the center of the main dialog
void QuizApp::moveDialog(QDialog * dialog)
{
    // First find out the center of the dialog
    // with respect to the screen and not relative to itself
    QPoint centerOfDialog = dialog->geometry().center() - dialog->geometry().topLeft();
    int x = center.x() - centerOfDialog.x();
    int y = center.y() - centerOfDialog.y();
    dialog->move(x, y);
}

// Called when we click Select Images/Select Amino Acids button
void QuizApp::showSelectImagesDialog()
{
    // There are two scenarios when we would want to show this dialog
    // One is if the button is clicked directly. Which is this case. The
    // other is if the user tried to start a quiz/test or study session
    // without selecting an Images. The user will be notified that
    // in order to start a quiz/test or study session he would have to chose
    // images first. If the user agreess then the user will be shown this dialog.

    // Since this is scenario 1 we set the TryingToStudy
    // and TryingToTakeQuiz flags to false.
    SelectImagesDialog::instance(this)->setTryingToStudy(false);
    SelectImagesDialog::instance(this)->setTryingToTakeQuiz(false);

    // Position the dialog in the middle of the main dialog
    moveDialog(SelectImagesDialog::instance());

    // Show the dialog.
    SelectImagesDialog::instance()->exec();
}

// The Quiz and Test options are exactly the same
// except that in Test mode you don't have the option
// "Image naming with prompt"
void QuizApp::showOptionsDialog(bool mode)
{
    // Set the mode, Quiz or Test
    QuizOptionsDialog::instance()->setMode(mode);

    // Check if Amino Acids are selected. If 
    // they are the move the Quiz/Test options 
    // dialog to the center and show it.
    if (SelectImagesDialog::instance()->isChosenImages())
    {
        moveDialog(QuizOptionsDialog::instance());
        QuizOptionsDialog::instance()->exec();
    }

    // If they are not then ask the user
    // if he wants to select images or not.
    else
    {
        int yesno = QMessageBox::question( this, "No image(s) Selected",
            "<b>There are no images selected for studying.\n\
            Would you like to select images now?</b>",
            QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes );
        
        // If yes then show the select Amino Acid dialog
        if (yesno == QMessageBox::Yes)
        {
            // We set the TryingToTakeQuiz flag so that
            // once the user selects Amino Acids the user
            // can be shown the Quiz/Test options dialog
            SelectImagesDialog::instance()->setTryingToTakeQuiz(true);

            moveDialog(SelectImagesDialog::instance());
            SelectImagesDialog::instance()->exec();
        }
    }
}

// Called when we click Take Quiz
// button. Shows the Quiz Options dialog
void QuizApp::showQuizOptionsDialog()
{
    QuizOptionsDialog::instance()->clearComboBox();
    for(int i = 0; i < MAX_MODES; ++i)
        QuizOptionsDialog::instance()->addItemToComboBox(mode[i]);
    
    showOptionsDialog(true);
}

// Called when we click Take Test 
// button. Shows the Quiz Options dialog
void QuizApp::showTestOptionsDialog()
{
    QuizOptionsDialog::instance()->clearComboBox();
    for (int i = 1; i < MAX_MODES; ++i)
        QuizOptionsDialog::instance()->addItemToComboBox(mode[i]);
    
    showOptionsDialog(false);
}

// Called when we click the Study 
// button. Shows the study options dialog
void QuizApp::showStudyImagesDialog()
{
    // Check if Amino Acids are selected. If they are
    // the move the Study options dialog to the center and show it.
    if (SelectImagesDialog::instance()->isChosenImages())
    {
        moveDialog(StudyOptionsDialog::instance());
        StudyOptionsDialog::instance()->exec();
    }
    else
    {
        int yesno = QMessageBox::question( this, "No image(s) Selected",
            "<b>There are no images selected for studying.\n \
            Would you like to select images now<b>",
            QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes);
        
        // If yes then show the select Amino Acid dialog
        if (yesno == QMessageBox::Yes)
        {
            // We set the TryingToStudy flag so that
            // once the user selects Amino Acids the user
            // can be shown the Study options dialog
            SelectImagesDialog::instance()->setTryingToStudy(true);
            moveDialog(SelectImagesDialog::instance());
            SelectImagesDialog::instance()->exec();
        }
    }
}

// Show Advanced options
void QuizApp::showAdvancedOptionsDialog()
{
    moveDialog(AdvancedOptionsDialog::instance());
    AdvancedOptionsDialog::instance()->exec();
}

// Create all the actions for the menu option
void QuizApp::create_actions()
{
    exitAct = new QAction("E&xit", this);
    connect(exitAct, SIGNAL(triggered()), this, SLOT(close()));

    viewProgressAct = new QAction("&View Progress", this);
    viewProgressAct->setShortcut(tr("Ctrl+V" ));
    connect(viewProgressAct, SIGNAL(triggered()), this, SLOT(viewProgress()));

    imagesSelectionAct = new QAction("Image Selection", this);
    imagesSelectionAct->setShortcut(tr("Ctrl+R"));
    connect(imagesSelectionAct, SIGNAL(triggered()), this, SLOT(showSelectImagesDialog()));

    studyAct = new QAction("Study", this);
    studyAct->setShortcut(tr( "Ctrl+S"));
    connect(studyAct, SIGNAL(triggered()), this, SLOT(showStudyImagesDialog()));

    quizAct = new QAction("Quiz", this);
    quizAct->setShortcut(tr("Ctrl+Q"));
    connect(quizAct, SIGNAL(triggered()), this, SLOT(showQuizOptionsDialog()));

    testAct = new QAction("Test", this);
    testAct->setShortcut(tr("Ctrl+T"));
    connect(testAct, SIGNAL(triggered()), this, SLOT(showTestOptionsDialog()));

    advancedOptionsAct = new QAction("Advanced Options", this);
    connect(advancedOptionsAct, SIGNAL(triggered()), this, SLOT(showAdvancedOptionsDialog()));

    validateDatabaseAct = new QAction("Validate Database", this);
    connect(validateDatabaseAct, SIGNAL(triggered()), this, SLOT(validateDatabase()));

    helpAct = new QAction("Help", this );
    helpAct->setShortcut(Qt::Key_F1);
    connect(helpAct, SIGNAL(triggered()), this, SLOT(showHelp()));

    tutorialAct = new QAction("Tutorial", this);
    connect(tutorialAct, SIGNAL(triggered()), this, SLOT(showTutorial()));
}

// Create the menu
void QuizApp::create_menus()
{
    fileMenu = new QMenu("&File", this);
    fileMenu->addAction(viewProgressAct);
    fileMenu->addSeparator();
    fileMenu->addAction(exitAct);

    runMenu = new QMenu("&Run", this);
    runMenu->addAction(imagesSelectionAct);
    runMenu->addSeparator();
    runMenu->addAction(studyAct);
    runMenu->addAction(quizAct);
    runMenu->addAction(testAct);

    optionsMenu = new QMenu("&Options", this);
    optionsMenu->addAction(advancedOptionsAct);
    optionsMenu->addAction(validateDatabaseAct);

    helpMenu = new QMenu("&Help", this);
    helpMenu->addAction(helpAct);
    helpMenu->addAction(tutorialAct);

    menuBar()->addMenu(fileMenu);
    menuBar()->addMenu(runMenu);
    menuBar()->addMenu(optionsMenu);
    menuBar()->addMenu(helpMenu);
}

// Create the buttons
void QuizApp::create_buttons()
{
   QFont font("Times", 20, QFont::Bold);
   font.setPixelSize(ppp(20));

   selectionButton = new QPushButton("Image Selection", this);
   selectionButton->setFont(font);
   selectionButton->show();
   QObject::connect(selectionButton, SIGNAL( clicked()), this, SLOT(showSelectImagesDialog()));

   studyButton = new QPushButton("Study Images", this);
   studyButton->setFont(font);
   studyButton->show();
   QObject::connect(studyButton, SIGNAL(clicked()), this, SLOT(showStudyImagesDialog()));

   quizButton = new QPushButton("Take a Quiz", this);
   quizButton->setFont(font);
   quizButton->show();
   QObject::connect(quizButton, SIGNAL(clicked()), this, SLOT(showQuizOptionsDialog()));

   testButton = new QPushButton("Take a Test", this);
   testButton->setFont(font);
   testButton->show();
   QObject::connect(testButton, SIGNAL(clicked()), this, SLOT(showTestOptionsDialog()));
}

// Check if the database was modified.
bool QuizApp::databaseModified()
{
    QFile userFile;
    userFile.setFileName(fname + ".csv");

    // If unable to read the last modified date
    // to verify if the database was modified or
    // not since the last usage there offer to
    // validate the database anyway.
    if (!userFile.open(QIODevice::ReadOnly | QIODevice::Text))
    {
        switch (QMessageBox::warning(this, "Error reading user file",
            tr("<b>The database file may have been modified. You can \
                validate the database file at any time by clicking \"Validate Database\"\
                under the Options menu. Do you wish to validate the database file now?</b>" ),
            QMessageBox::Yes|QMessageBox::No,
            QMessageBox::Yes))
        {
        case QMessageBox::Yes:
            return true;
        case QMessageBox::No:
            return false;
        }
    }

    QTextStream in;
    in.setDevice(&userFile);
    in.readLine();    // Ignoring advanced options.
    QString lastModified = in.readLine();     // Read in last modifed date.

    QFileInfo fi((ResourceFolder() + "Database.csv").c_str());
    
    // If modifications are detected then ask the user
    // if he wants to validate the database or not
    if (lastModified != fi.lastModified().toString())
    {
        switch( QMessageBox::warning( this, "Warning",
            tr( "<b>The database file has been modified since the last session. You can \
                validate the database file at any time by clicking \"Validate Database\"\
                under the Options menu. Do you wish to validate the database file now?</b>" ),
            QMessageBox::Yes | QMessageBox::No,
            QMessageBox::Yes))
        {
        case QMessageBox::Yes:
            return true;
        case QMessageBox::No:
            return false;
        }
    }
    return false;
}

void QuizApp::validateDatabase()
{
    // Set up the progress bar dialog.
    progress = new QProgressDialog("Checking database for errors", "Cancel", 0, 100000, this);
    progress->setWindowModality(Qt::WindowModal);
    progress->setMinimumDuration(0);
    progress->setCancelButton(NULL);

    QFile dataFile;
    dataFile.setFileName((ResourceFolder() + "Database.csv").c_str());
    if (!dataFile.open( QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning( this, "Invalid Filename", tr( "<b>Database cannot be opened for reading. Try again later.</b>"));
        return;
    }

    QTextStream in;
    in.setDevice(&dataFile);
    QString line = in.readLine();
    QStringList columns = line.split( "," );
    int col = columns.size();  // number of columns
    int row = 0;               // row being validated
    int errors = 0;            // number of errors found
    bool atLeastOneValidImage = false;     // to terminate program is no valid images are found
    bool error = false;        //whether an error was found or not;

    if (col < 3)  // Filename, Image name and at least one property
    {
        QMessageBox::critical(this, "Invalid Header",
            tr("<b>The header should have at least three fields - \
                file name, image name and at least one property. Terminating program.</b>" ));
        QCoreApplication::quit();
    }

    QFile logFile;
    logFile.setFileName((UserDataFolder() + "log.txt").c_str());
    logFile.open(QIODevice::WriteOnly | QIODevice::Text );
    QTextStream out;
    out.setDevice(&logFile);
    out << QDateTime::currentDateTime().toString() << "\n";

    progress->setMinimum(0);
    progress->setMaximum(col);
    QImageReader testImage;

    while (!in.atEnd())
    {
        row++;
        line = in.readLine();
        columns = line.split(",");
        QString labelText = QString( "<b>Validating database row %1.</b>" ).arg( row ) + QString( "<b> %1 errors found.</b>" ).arg(errors);
        progress->setLabelText(labelText);
        progress->setValue(1);

        // The assumption is that each row will
        // will have the same number of fields
        // as the header.
        if (col != columns.size())
        {
            error = true;
            out << "Wrong number of fields - " << columns.size() << " Expected - " << col;
        }
        
        progress->setValue(col / 4);
        QString imageFileName = "Images/" + columns.at(0) + ".jpg";
        testImage.setFileName((ResourceFolder() + imageFileName.toStdString()).c_str());

        // Check if the image loads. canRead
        // doesn't load the whole file, just
        // reads the image file header.
        if (!testImage.canRead())
        {
            error = true;
            out << imageFileName << " failed to load. "; 
        }
        
        if (error)
        {
            errors++;
            error = false;
            out << "Line : " << row + 1 << "\n";
        }
        else
            atLeastOneValidImage = true;
        progress->setValue(col - 1);
    }
    progress->setValue(col);

    // Terminate the program if no valid images were found
    if (!atLeastOneValidImage)
    {
        QMessageBox::critical( this, "Fatal error occured",
            tr( "<b>All the images failed to load. Read log.txt for details. \
                Terminating program.</b>" ) );
        QCoreApplication::quit();
    }

    // Let the user know if there were errors of not.
    if (errors == 0)
    {
        QMessageBox::information(this, "Database Validation Complete",
            tr("<b>No errors were found.</b>"));
        return;
    }
    else
    {
        QMessageBox::warning(this, "Database Validation Complete",
            tr("<b>Errors were found. Read log.txt for details.</b>"));
        return;
    }
    
    logFile.close();
    dataFile.close();
}

// Called when the user logs in
void QuizApp::init(QString username)
{
    // Set the size of the Study and Quiz dialog
    // to the size of the main screen
    StudyDialog::instance(this)->setSize(size());
    QuizDialog::instance(this)->setSize(size());

    moveDialog(AdvancedOptionsDialog::instance(this));

    // Read the advanced options from the last 
    // saved session and restore those setting
    AdvancedOptionsDialog::instance()->updateVariables(username);

    // Set the flag that user has
    // successfully logged in
    loggedIn = true;

    // Hide the login widgets
    loginGroupBox->hide();
    newUserGroupBox->hide();

    if (databaseModified())
        validateDatabase();

    create_buttons();
    create_actions();
    create_menus();
    updateCenter();

    QuizDialog::instance()->setUsername(username);

    resizeEvent(0);
}

// Called when the user tries to login
void QuizApp::login()
{
    // User information is stored in user files
    // in the 'User Files' folder with the name
    // <username>.csv
    fname = (UserDataFolder() + loginLineEdit->text().toStdString()).c_str();
    QFile inFile(fname + ".csv");
    if (!inFile.exists())
    {
        QMessageBox::warning(this, "User does not exist! ",
            tr( "<b>Your file was not found. If you do not have a file \
                please create one by using the create new user box on the right.</b>" ));
        loginLineEdit->selectAll();
    }
    else
      init(fname);
}

// Called when the user tries to create a new user
void QuizApp::createNewUser()
{
    QString username = newUserLineEdit->text().simplified();
    if (username == "")
    {
        QMessageBox::critical(this, "Error", tr("<b>Invalid username.</b>").arg(username));
        return;
    }
    fname = (UserDataFolder() + username.toStdString()).c_str();
    QFile file(fname + ".csv");
    if (file.exists())
    {
        QMessageBox::warning(this, "User already exists!",
            tr( "<b>The username %1 already exists. \
                Please use another username.</b>").arg(username));
        newUserLineEdit->selectAll();
    }
    else
    {
        if (file.open( QIODevice::WriteOnly | QIODevice::Text))
        {
            QMessageBox::information(this, "New user created!",
                tr( "<b>Username (%1) has been successfully created. \
                    Please remember your username.</b>" ).arg( username ) );
            init(fname);
            file.close();
        }
    }
}

// Organize the login related widgets
void QuizApp::createLoginScreen()
{
    QFont font("Times", 15, QFont::Bold);
    font.setPixelSize(ppp(15));

    loginGroupBox = new QGroupBox("Existing Users Login Here.", this);
    loginGroupBox->setFont(font);

    label1 = new QLabel("Username:", loginGroupBox);
    label1->setGeometry(QRect(ppp(10), ppp(25), ppp(100), ppp(25)));
    label1->setFont(font);
    label1->show();

    loginLineEdit = new QLineEdit( loginGroupBox );
    loginLineEdit->setGeometry(QRect(ppp(105), ppp(25), ppp(180), ppp(25)));
    loginLineEdit->setFont( font );
    loginLineEdit->setFocus();
    loginLineEdit->show();
    QObject::connect(loginLineEdit, SIGNAL(returnPressed()), this, SLOT(login()));

    loginButton = new QPushButton("Login", loginGroupBox);
    loginButton->setGeometry(QRect(ppp(10), ppp(60), ppp(280), ppp(30)));
    loginButton->setFont( font );
    QObject::connect(loginButton, SIGNAL(clicked()), this, SLOT(login()));
    loginButton->setFont(font);
    loginButton->show();

    newUserGroupBox = new QGroupBox("Create a New User Here.", this);
    newUserGroupBox->setFont(font);

    label2 = new QLabel( "Username:", newUserGroupBox );
    label2->setGeometry(QRect(ppp(10), ppp(25), ppp(100), ppp(25)));
    label2->setFont(font);
    label2->show();

    newUserLineEdit = new QLineEdit( newUserGroupBox );
    newUserLineEdit->setGeometry(QRect(ppp(105), ppp(25), ppp(180), ppp(25)));
    newUserLineEdit->setFont(font);
    newUserLineEdit->show();
    QObject::connect(newUserLineEdit, SIGNAL(returnPressed()), this, SLOT(createNewUser()));

    newUserButton = new QPushButton("Create New User", newUserGroupBox);
    newUserButton->setGeometry(QRect(ppp(10), ppp(60), ppp(280), ppp(30)));
    newUserButton->setFont(font);
    newUserButton->show();
    QObject::connect(newUserButton, SIGNAL(clicked()), this, SLOT(createNewUser()));
    QObject::connect(newUserButton, SIGNAL(activated()), this, SLOT(createNewUser()));
}

// Called when the X in the upper left is clicked
void QuizApp::closeEvent(QCloseEvent * e)
{
    if (QMessageBox::question( this, "Quit", "<b>Are you sure?</b>",
        QMessageBox::Yes | QMessageBox::No,
        QMessageBox::Yes) == QMessageBox::Yes)
    {
        if (loggedIn)
            updateUserData();
    }
    else
        e->ignore();
}

QuizApp::QuizApp()
{
    setWindowTitle("Image Quiz");
    loggedIn = false;
    terminateNow = false;
    createLoginScreen();

    setFocusPolicy(Qt::StrongFocus);
    setMinimumSize(MIN_WIDTH, MIN_HEIGHT);
    showMaximized();
}

QuizApp::~QuizApp()
{
}
