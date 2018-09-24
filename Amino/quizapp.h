#pragma once

#include <QMainWindow>
#include <QPushButton>
#include <QLabel>
#include <QComboBox>
#include <QDialogButtonBox>
#include <QGroupBox>
#include <QDialogButtonBox>
#include <QCheckBox>
#include <QListWidget>
#include <QGridLayout>

class QuizApp : public QMainWindow
{
    Q_OBJECT

private:
    QMenu * fileMenu;
    QMenu * runMenu;
    QMenu * optionsMenu;
    QMenu * helpMenu;
    QAction * exitAct;
    QAction * viewProgressAct;
    QAction * aminoAcidSelectionAct;
    QAction * studyAct;
    QAction * quizAct;
    QAction * testAct;
    QAction * advancedOptionsAct;
    QAction * helpAct;
    QAction * tutorialAct;

    QPushButton * selectionButton;
    QPushButton * studyButton;
    QPushButton * quizButton;
    QPushButton * testButton;

    QLineEdit * loginLineEdit;
    QLineEdit * newUserLineEdit;
    QGroupBox * loginGroupBox;
    QGroupBox * newUserGroupBox;
    QLabel * label1;
    QLabel * label2;
    QPushButton * loginButton;
    QPushButton * newUserButton;

    void create_actions();
    void create_menus();
    void create_buttons();
    void createLoginScreen();
    
    QPoint center;
    void updateCenter();
    bool loggedIn;

    QString fname;
    void init(QString);
    void moveDialog(QDialog *);
    void showOptionsDialog(bool);
    void updateUserData();
    void writeUserData(QStringList);

public:
    QuizApp();
    ~QuizApp();

public slots:
    bool exit();
    void viewProgress();
    void resizeEvent(QResizeEvent *);
    void moveEvent(QMoveEvent *);
    void showQuizOptionsDialog();
    void showTestOptionsDialog();
    void showSelectAminoAcidDialog();
    void showStudyAminoAcidDialog();
    void showAdvancedOptionsDialog();
    void showHelp();
    void showTutorial();
    void login();
    void createNewUser();
    void closeEvent(QCloseEvent * e);
};