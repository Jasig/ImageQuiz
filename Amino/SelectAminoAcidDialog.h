#include <QDialog>
#include <QLabel>
#include <QListWidget>
#include <QPushButton>
#include <QGroupBox>
#include <QRadioButton>
#include <QStringList>
#include <QMessageBox>

#define MAX_PROPERTY_TYPES 6
#define MAX_IMAGE_TYPES 5
#define MAX_NAME_TYPES 4

class SelectAminoAcidDialog:
   public QDialog
{
   Q_OBJECT
      static SelectAminoAcidDialog *s_instance;

   QLabel *label;
   QListWidget *choseAAListWidget;
   QListWidget *selectedAAListWidget;
   QPushButton *addAllButton;
   QPushButton *addButton;
   QPushButton *removeButton;
   QPushButton *removeAllButton;
   QPushButton *finishButton;
   QPushButton *cancelButton;
   QGroupBox *aAGroupBox;

   QGroupBox *propertiesGroupBox;
   QRadioButton *propertyTypeRadioButton[ MAX_PROPERTY_TYPES ];

   QGroupBox *imageTypeGroupBox;
   QRadioButton *imageTypeRadioButton[ MAX_IMAGE_TYPES ];

   QGroupBox *nameTypeGroupBox;
   QRadioButton *nameTypeRadioButton[ MAX_NAME_TYPES ];
   
   bool propertyTypeChecked[ MAX_PROPERTY_TYPES ];
   bool imageTypeChecked[ MAX_IMAGE_TYPES ];
   bool nameTypeChecked[ MAX_NAME_TYPES ];

   bool tryingToStudy;
   bool tryingToTakeQuiz;

   void addListWidgetItems();
   QStringList chooseFromList;
   QStringList chosenList;

   SelectAminoAcidDialog( QWidget *parent = 0 );
   ~SelectAminoAcidDialog( void );

public:
   static SelectAminoAcidDialog *instance( QWidget *parent = 0 );
   bool isChosenAminoAcids();
   void setTryingToStudy( bool );
   void setTryingToTakeQuiz( bool );
   void setImageType();
   void setNameType();
   void setPropertyType();
   void conclude();

   public slots:
      void addAll();
      void add();
      void remove();
      void removeAll();
      
      void noProperty();
      void noName();

      void finish();

      void closeEvent( QCloseEvent * );
      void reject();
};
