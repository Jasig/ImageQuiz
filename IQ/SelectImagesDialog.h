#include <QDialog>
#include <QLabel>
#include <QListWidget>
#include <QPushButton>
#include <QGroupBox>
#include <QRadioButton>
#include <QStringList>
#include <QMessageBox>
#include <QComboBox>

#define MAX_ANSWER_TYPES 3

class SelectImagesDialog:
   public QDialog
{
   Q_OBJECT
      static SelectImagesDialog *s_instance;

   QLabel *label;
   QListWidget *choseImagesListWidget;
   QListWidget *selectedImagesListWidget;
   QPushButton *addAllButton;
   QPushButton *addButton;
   QPushButton *removeButton;
   QPushButton *removeAllButton;
   QPushButton *finishButton;
   QGroupBox *imagesGroupBox;

   QGroupBox *propertiesGroupBox;
   QComboBox *propertiesComboBox;
   int propertyIndex;
   
   QGroupBox *answerTypeGroupBox;
   QRadioButton *answerTypeRadioButton[ MAX_ANSWER_TYPES ];

   bool tryingToStudy;
   bool tryingToTakeQuiz;
   
   //int answerType;      //Name and property, only name or only property.
   bool answerTypeChecked[ MAX_ANSWER_TYPES ];    //State of each radio button.

   void addComboBoxItems();
   void addListWidgetItems();
   QStringList chooseFromList;
   QStringList chosenList;

   SelectImagesDialog( QWidget *parent = 0 );
   ~SelectImagesDialog( void );

public:
   static SelectImagesDialog *instance( QWidget *parent = 0 );
   bool isChosenImages();
   void setTryingToStudy( bool );
   void setTryingToTakeQuiz( bool );
   void conclude();

   public slots:
      void addAll();
      void add();
      void remove();
      void removeAll();
      
      void setPropertyType( int );
      void setAnswerMode();

      void finish();

      void closeEvent( QCloseEvent * );
      void reject();
};
