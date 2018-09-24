#ifndef QUIZAPP_H
#define QUIZAPP_H

#include <QMainWindow>
#include <QPushButton>
#include <QLabel>
#include <QImage>
#include <QImageReader>
#include <QComboBox>
#include <QDialogButtonBox>
#include <QGroupBox>
#include <QDialogButtonBox>
#include <QCheckBox>
#include <QListWidget>
#include <QGridLayout>
#include <QProgressDialog>

class QuizApp: public QMainWindow
{
   Q_OBJECT

private:

   QMenu *fileMenu; //File menu
   QMenu *runMenu; //Run menu
   QMenu *optionsMenu; //Options menu
   QMenu *helpMenu; //Help menu
   QAction *exitAct; // Exit menu entry
   QAction *viewProgressAct; //View progress menu entry
   QAction *imagesSelectionAct; //Amino Acid selection menu entry
   QAction *studyAct; //Study menu entry
   QAction *quizAct; //Quiz menu entry
   QAction *testAct; //Test menu entry
   QAction *advancedOptionsAct; //Advanced Options menu entry
   QAction *validateDatabaseAct; //Validate database menu entry
   QAction *helpAct; //Help menu entry
   QAction *tutorialAct; //Tutorial menu entry

   QPushButton *selectionButton; //Selection Amino Acids button
   QPushButton *studyButton; //Study Amino Acids button
   QPushButton *quizButton; //Take Quiz button
   QPushButton *testButton; //Take Test button

   QProgressDialog *progress; //Checking database progress bar

   QLineEdit *loginLineEdit; //Textbox to login
   QLineEdit *newUserLineEdit; //Textbox to create new user
   QGroupBox *loginGroupBox;
   QGroupBox *newUserGroupBox;
   QLabel *label1;
   QLabel *label2;
   QPushButton *loginButton; //Login button
   QPushButton *newUserButton; //Create new user button
   
   void create_actions(); // Create menu actions.
   void create_menus(); // Create menubar.
   void create_buttons(); // Create button.
   void createLoginScreen(); //Create login screen


   QPoint center; //Current center of the application
   void updateCenter(); //Update center when the is moved
   bool loggedIn;    //Logged in or not
   bool terminateNow;    //to terminate the program unconditionally

   QString fname; //user file name
   void init( QString ); //initialize method
   void moveDialog( QDialog* ); //move dailog
   void showOptionsDialog( bool );  //Show Test or Quiz options
   void updateUserData(); //save user data
   void writeUserData( QStringList ); //write user data to file.
   bool databaseModified(); //to test if database was modified since last session.


public:
   QuizApp();
   ~QuizApp();

   public slots:
      void viewProgress(); //View progress
      void resizeEvent( QResizeEvent * ); //Resize dialog
      void moveEvent( QMoveEvent * ); //move dialog
      void showQuizOptionsDialog(); //show quiz options dialog
      void showTestOptionsDialog(); //show test options dialog
      void showSelectImagesDialog();  //show dialog to select amino acids
      void showStudyImagesDialog();  //show study options dialog
      void showAdvancedOptionsDialog();  //show advanced options dialog
      void validateDatabase();  //validate database
      void showHelp();  //show help
      void showTutorial();  //show tutorial
      void login();     //log in
      void createNewUser();      //create a new user
      void closeEvent( QCloseEvent* e );  //close dialog
};
#endif