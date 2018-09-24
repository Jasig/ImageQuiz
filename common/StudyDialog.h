#include <QFile>
#include <QLabel>
#include <QDialog>
#include <QPixmap>
#include <QMessageBox>
#include <QKeyEvent>
#include <QTimer>
#include <QPixmap>
#include <QFont>
#include <QShortcut>

class StudyDialog :
   public QDialog
{
   Q_OBJECT

   static StudyDialog *s_instance;
   QLabel *imageLabel;
   QLabel *captionLabel;
   QTimer *timer;
   QShortcut *shortcut;
   StudyDialog( QWidget *parent = 0 );
   bool timerSet;
   bool firstTime;

   int orderOption;
   int nameOption1;
   int nameOption2;
   int interactionOption;
   int stopOption;
   int interval;
   int fixationInterval;
   int intervalCounter;
   int currentImageIndex;
   int numberOfImages;
   QImage image;
   QFont font;

   QPalette initialPalette;
   int backgroundColor;
   QString changeFontColor( QString );
   void loadImage( QString );
   void organizeDialog();

protected:
   void  keyPressEvent( QKeyEvent * );	// Handle keystroke events

public:
   QStringList filenameList;
   QStringList captionList;
   QStringList imagePropertyList;
   QList<int> indexList;
   
   ~StudyDialog( void );
   static StudyDialog* instance( QWidget *parent = 0 );
   void initialize();
   void setSize( QSize );
   void setOrderOption( int );
   void setNameOption1( int );
   void setNameOption2( int );
   void setStopOption ( int );
   void setInteractionOption( int );
   void setInterval( int );
   void setFixationInterval( int );
   void setBackgroundColor( int );


   public slots:
      void close();
      void updateLabel();
      void closeEvent( QCloseEvent * );
};
