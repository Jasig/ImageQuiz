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
#include "AminoAcidList.h"
#include "SelectAminoAcidDialog.h"
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

void QuizApp::writeUserData(QStringList records)
{
    QFile outFile;
    outFile.setFileName(fname + ".csv");

    // If for whatever reason current user file could not be opened
    // then current user session info will not be saved
    if (!outFile.open( QIODevice::WriteOnly | QIODevice::Text))
    {
        QMessageBox::warning(this, "Error", tr("Could NOT save this session"));
        return;
    }

    QTextStream out;
    out.setDevice(&outFile);

    // First record is Advanced settings
    // Every other line after that is a progress report
    for (int i = 0; i < records.size(); ++i)
    {
        out << records[ i ].trimmed();
        out << "\n";
    }

    outFile.close();
}

// Create a StringList to be written to file
// Only up to MAX_REPORTS reports are allowed
void QuizApp::updateUserData()
{
    // Stores the advanced options of the current session followed by the current progress reports and
    // then the rest of the progress reports read from the existing user file.
    QStringList records;

    // Save advanced options
    records << AdvancedOptionsDialog::instance()->getSettings();
    QStringList currentRecords = QuizDialog::instance()->getCurrentRecords();

    int reportCounter = 0;
    int n = currentRecords.size();

    // If there are progress reports of the currect session save those
    if (n != 0)
    {
        for (int i = n - 1; i >= 0 && reportCounter < MAX_REPORTS; --i)
        {
            records << currentRecords[i];
            reportCounter++;
        }
    }

    // After saving all the data for the current session
    // we append to the end of the list all the previous user data
    QFile inFile;
    inFile.setFileName(fname + ".csv");
    if (!inFile.open( QIODevice::ReadOnly | QIODevice::Text))
    {
        QMessageBox::warning( this, "Error", tr( "Cannot open %1 for reading." ).arg(fname + ".csv"));
        inFile.close();

        // If the saved user file could not be read try to save the current session
        writeUserData(records);
        return;
    }

    QTextStream in;
    in.setDevice(&inFile);
    QString line = in.readLine();
    while (!in.atEnd() && reportCounter < MAX_REPORTS)
    {
        line = in.readLine();
        if (!line.isEmpty())
        {
            records << line;
            ++reportCounter;
        }
        else
            break;
    }
    inFile.close();

    // Write all the data back to the user file
    writeUserData(records);
}

// Exit the program
bool QuizApp::exit()
{
    int yesNo;

    yesNo = QMessageBox::question(this, "Quit", "Are you sure?", QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes);

    if (yesNo == QMessageBox::Yes)
    {
        // If the user is logged in then save the current session info
        // Otherwise just quit
        if (loggedIn)
            updateUserData();
        return close();
    }

    return false;
}

// View the Progress Report
void QuizApp::viewProgress()
{
    // Move the dialog to the center of the screen
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
    QDesktopServices::openUrl(QUrl(urlString));
}

// Show tutorial page
void QuizApp::showTutorial()
{
    QString urlString = ResourceFolder().c_str();
    urlString = "file:///" + urlString + "Help/";
    urlString += "Tutorial.html";
    QDesktopServices::openUrl(QUrl(urlString));
}

// Reset the dialog positions when the main dialog is moving
void QuizApp::updateCenter()
{
    QRect rect = geometry();
    center.setX(rect.center().x());
    center.setY(rect.center().y());

    ProgressReport::instance(this)->setCoordinates(center.x(), center.y());
    StudyOptionsDialog::instance(this)->setCoordinates(center.x(), center.y());
    QuizOptionsDialog::instance(this)->setCoordinates(center.x(), center.y());
}

// Handle main dialog resize event
void QuizApp::resizeEvent(QResizeEvent * e)
{
    QSize windowSize = size();
    int x = windowSize.width() / 2;
    int y = windowSize.height() / 2;

    // If the user is not logged in then setup login widgets otherwise quiz and study widgets.
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
}

// Handle main dialog move event
void QuizApp::moveEvent(QMoveEvent * m)
{
    updateCenter();
}

// Reposition the dialog so that it is in the center of the main dialog
void QuizApp::moveDialog(QDialog * dialog)
{
    // First find out the center of the dialog
    // with respect to the screen and not relative to itself
    QPoint centerOfDialog = dialog->geometry().center() - dialog->geometry().topLeft();

    int x = center.x() - centerOfDialog.x();
    int y = center.y() - centerOfDialog.y();
    dialog->move(x, y);
}

void QuizApp::showSelectAminoAcidDialog()
{
    // There are two scenarios when we would want to show this dialog
    // One is if the button is clicked directly. Which is this case. The
    // other is if the user tried to start a quiz/test or study session
    // without selecting an amino acids. The user will be notified that
    // in order to start a quiz/test or study session he would have to chose
    // amino acids first. If the user agreess then the user will be shown
    // this dialog

    // Since this is scenario 1 we set the TryingToStudy
    // and TryingToTakeQuiz flags to false.
    SelectAminoAcidDialog::instance(this)->setTryingToStudy(false);
    SelectAminoAcidDialog::instance(this)->setTryingToTakeQuiz(false);

    // Position the dialog in the
    // middle of the main dialog
    moveDialog(SelectAminoAcidDialog::instance());

    // Show the dialog.
    SelectAminoAcidDialog::instance()->exec();
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
    if (SelectAminoAcidDialog::instance()->isChosenAminoAcids())
    {
        moveDialog( QuizOptionsDialog::instance());
        QuizOptionsDialog::instance()->exec();
    }

    // If they are not then ask the user
    // if he wants to select Amino Acids or not
    else
    {
        int yesno = QMessageBox::question(this,
            "No Amino Acids Selected",
            "There are no Amino Acids selected for studying.\n"
            "Would you like to select Amino Acids now?",
            QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes
        );
        
        // If yes then show the select Amino Acid dialog
        if ( yesno == QMessageBox::Yes )
        {
            // We set the TryingToTakeQuiz flag so that
            // once the user selects Amino Acids the user
            // can be shown the Quiz/Test options dialog
            SelectAminoAcidDialog::instance()->setTryingToTakeQuiz(true);

            moveDialog(SelectAminoAcidDialog::instance());
            SelectAminoAcidDialog::instance()->exec();
        }
    }
}

void QuizApp::showQuizOptionsDialog()
{
    QuizOptionsDialog::instance()->clearComboBox();
    
    for (int i = 0; i < MAX_MODES; ++i)
        QuizOptionsDialog::instance()->addItemToComboBox(mode[i]);
    
    showOptionsDialog(true);
}

void QuizApp::showTestOptionsDialog()
{
    QuizOptionsDialog::instance()->clearComboBox();
    
    for (int i = 1; i < MAX_MODES; ++i)
        QuizOptionsDialog::instance()->addItemToComboBox(mode[i]);
    
    showOptionsDialog(false);
}

void QuizApp::showStudyAminoAcidDialog()
{
    // Check if Amino Acids are selected. If they are - move
    // the Study options dialog to the center and show it
    if (SelectAminoAcidDialog::instance()->isChosenAminoAcids())
    {
        moveDialog( StudyOptionsDialog::instance() );
        StudyOptionsDialog::instance()->exec();
    }
    else
    {
        int yesno = QMessageBox::question(this,
            "No Amino Acids Selected",
            "There are no Amino Acids selected for studying.\n"
            "Would you like to select Amino Acids now?",
            QMessageBox::Yes | QMessageBox::No, QMessageBox::Yes
        );
      
        // If yes then show the select Amino Acid dialog
        if (yesno == QMessageBox::Yes)
        {
            // We set the TryingToStudy flag so that
            // once the user selects Amino Acids the user
            // can be shown the Study options dialog
            SelectAminoAcidDialog::instance()->setTryingToStudy(true);
            moveDialog(SelectAminoAcidDialog::instance());
            SelectAminoAcidDialog::instance()->exec();
        }
    }
}

void QuizApp::showAdvancedOptionsDialog()
{
    moveDialog(AdvancedOptionsDialog::instance());
    AdvancedOptionsDialog::instance()->exec();
}

void QuizApp::create_actions()
{
    exitAct = new QAction("E&xit", this);
    connect(exitAct, SIGNAL(triggered()), this, SLOT(close()));

    viewProgressAct = new QAction("&View Progress", this);
    viewProgressAct->setShortcut(tr("Ctrl+V"));

    connect(viewProgressAct, SIGNAL(triggered()), this, SLOT(viewProgress()));

    aminoAcidSelectionAct = new QAction("Amino Acid Selection", this);
    aminoAcidSelectionAct->setShortcut(tr("Ctrl+R"));

    connect(aminoAcidSelectionAct, SIGNAL(triggered()), this, SLOT(showSelectAminoAcidDialog()));

    studyAct = new QAction("Study", this );
    studyAct->setShortcut( tr("Ctrl+S"));

    connect(studyAct, SIGNAL(triggered()), this, SLOT(showStudyAminoAcidDialog()));

    quizAct = new QAction("Quiz", this);
    quizAct->setShortcut(tr("Ctrl+Q"));

    connect(quizAct, SIGNAL(triggered()), this, SLOT(showQuizOptionsDialog()));

    testAct = new QAction( "Test", this );
    testAct->setShortcut(tr("Ctrl+T"));

    connect(testAct, SIGNAL(triggered()), this, SLOT(showTestOptionsDialog()));

    advancedOptionsAct = new QAction("Advanced Options", this);
    connect(advancedOptionsAct, SIGNAL(triggered()), this, SLOT(showAdvancedOptionsDialog()));

    helpAct = new QAction( "Help", this );
    helpAct->setShortcut( Qt::Key_F1 );

    connect(helpAct, SIGNAL(triggered()), this, SLOT(showHelp()));

    tutorialAct = new QAction( "Tutorial", this );

    connect(tutorialAct, SIGNAL(triggered()), this, SLOT(showTutorial()));
}

void QuizApp::create_menus()
{
    fileMenu = new QMenu("&File", this);
    fileMenu->addAction(viewProgressAct);
    fileMenu->addSeparator();
    fileMenu->addAction(exitAct);

    runMenu = new QMenu("&Run", this);
    runMenu->addAction( aminoAcidSelectionAct );
    runMenu->addSeparator();
    runMenu->addAction(studyAct);
    runMenu->addAction(quizAct);
    runMenu->addAction(testAct);

    optionsMenu = new QMenu("&Options", this);
    optionsMenu->addAction(advancedOptionsAct);

    helpMenu = new QMenu("&Help", this);
    helpMenu->addAction(helpAct);
    helpMenu->addAction(tutorialAct);

    menuBar()->addMenu(fileMenu);
    menuBar()->addMenu(runMenu);
    menuBar()->addMenu(optionsMenu);
    menuBar()->addMenu(helpMenu);
}

void QuizApp::create_buttons()
{
    QFont font("Times", 20, QFont::Bold);
    font.setPixelSize(ppp(20));

    selectionButton = new QPushButton("Amino Acid Selection", this);
    selectionButton->setFont(font);
    selectionButton->show();

    QObject::connect(selectionButton, SIGNAL(clicked()), this, SLOT(showSelectAminoAcidDialog()));

    studyButton = new QPushButton("Study Amino Acids", this);
    studyButton->setFont( font );
    studyButton->show();

    QObject::connect(studyButton, SIGNAL(clicked()), this, SLOT(showStudyAminoAcidDialog()));

    quizButton = new QPushButton("Take a Quiz", this);
    quizButton->setFont(font);
    quizButton->show();

    QObject::connect(quizButton, SIGNAL(clicked()), this, SLOT(showQuizOptionsDialog()));

    testButton = new QPushButton("Take a Test", this );
    testButton->setFont(font);
    testButton->show();

    QObject::connect(testButton, SIGNAL(clicked()), this, SLOT(showTestOptionsDialog()));
}

// Called when the user logs in
void QuizApp::init(QString username)
{
    // Set the size of the Study and Quiz dialog
    // to the size of the main screen
    StudyDialog::instance(this)->setSize(size());
    QuizDialog::instance(this)->setSize(size());

    // Read the advanced options from the last 
    // saved session and restore those setting
    AdvancedOptionsDialog::instance(this)->updateVariables(username);

    // Set the flag that user has successfully logged in
    loggedIn = true;

    // Hide the login widgets
    loginGroupBox->hide();
    newUserGroupBox->hide();

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
    // in the 'Amino' folder with the name
    // <username>.csv
    fname = (UserDataFolder()).c_str() + loginLineEdit->text();
    QFile inFile(fname + ".csv");
    if (!inFile.exists())
    {
        QMessageBox::warning(this, "User does not exist! ", tr(
            "<b>Your file was not found. "
            "If you do not have a file please create one by "
            "using the create new user box on the right.</b>"
        ));
        loginLineEdit->selectAll();
    }
    else
        init(fname);
}

// Called when the user tries to create a new user
void QuizApp::createNewUser()
{
    QString username = newUserLineEdit->text();
    fname = (UserDataFolder()).c_str() + username;
    qDebug() << "creating user file " << fname;
    QFile file(fname + ".csv");
    if (file.exists())
    {
        QMessageBox::warning(this, "User already exists!", tr(
            "<b>The username %1 already exists. "
            "Please use another username.</b>"
            ).arg(username)
        );
        newUserLineEdit->selectAll();
    }
    else
    {
        if (file.open(QIODevice::WriteOnly | QIODevice::Text))
        {
            QMessageBox::information(this, "New user created!", tr(
                "<b>Username (%1) has been successfully created. "
                "Please remember your username.</b>"
                ).arg(username)
            );
            
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

    loginLineEdit = new QLineEdit(loginGroupBox);
    loginLineEdit->setGeometry(QRect(ppp(105), ppp(25), ppp(180), ppp(25)));
    loginLineEdit->setFont(font);
    loginLineEdit->setFocus();
    loginLineEdit->show();

    QObject::connect(loginLineEdit, SIGNAL(returnPressed()), this, SLOT(login()));

    loginButton = new QPushButton("Login", loginGroupBox);
    loginButton->setGeometry(QRect(ppp(10), ppp(60), ppp(280), ppp(30)));
    loginButton->setFont(font);
    loginButton->show();

    QObject::connect(loginButton, SIGNAL(clicked()), this, SLOT(login()));

    newUserGroupBox = new QGroupBox("Create a New User Here.", this);
    newUserGroupBox->setFont(font);

    label2 = new QLabel("Username:", newUserGroupBox);
    label2->setGeometry(QRect(ppp(10), ppp(25), ppp(100), ppp(25)));
    label2->setFont(font);
    label2->show();

    newUserLineEdit = new QLineEdit(newUserGroupBox);
    newUserLineEdit->setGeometry(QRect(ppp(105), ppp(25), ppp(180), ppp(25)));
    newUserLineEdit->setFont(font);
    newUserLineEdit->show();

    QObject::connect(newUserLineEdit, SIGNAL(returnPressed()), this, SLOT(createNewUser()));

    newUserButton = new QPushButton( "Create New User", newUserGroupBox );
    newUserButton->setGeometry(QRect(ppp(10), ppp(60), ppp(280), ppp(30)));
    newUserButton->setFont(font);
    newUserButton->show();

    QObject::connect(newUserButton, SIGNAL(clicked()), this, SLOT(createNewUser()));
}

// Called when the X in the upper left is clicked
void QuizApp::closeEvent(QCloseEvent * e)
{
    if (!exit())
        e->ignore();
}

QuizApp::QuizApp()
{
    // Keyboard focus to get keystrokes
    setFocusPolicy(Qt::StrongFocus); 
    
    setMinimumSize(MIN_WIDTH, MIN_HEIGHT);
    
    loggedIn = false;
    
    createLoginScreen();
    
    showMaximized();
    
    setWindowTitle("Amino");
}

QuizApp::~QuizApp()
{
}
